/*
 * Copyright 2014 Tagbangers, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wallride.service.impl;

import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.model.GaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.wallride.domain.*;
import org.wallride.exception.GoogleAnalyticsException;
import org.wallride.exception.ServiceException;
import org.wallride.model.PostSearchRequest;
import org.wallride.repository.PopularPostRepository;
import org.wallride.repository.PostRepository;
import org.wallride.service.PostService;
import org.wallride.support.GoogleAnalyticsUtils;
import org.wallride.web.controller.guest.article.ArticleDescribeController;
import org.wallride.web.controller.guest.page.PageDescribeController;
import org.wallride.web.support.BlogLanguageRewriteMatch;
import org.wallride.web.support.BlogLanguageRewriteRule;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {

	private static Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private ServletContext servletContext;

	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private JobExplorer jobExplorer;
	@Autowired
	private Job updatePostViewsJob;

	@Autowired
	private BlogService blogService;

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PopularPostRepository popularPostRepository;


	@Override
	public List<Post> publishScheduledPosts() {
		logger.info("Starting public posts of the scheduled");

		LocalDateTime now = LocalDateTime.now();
		List<Post> posts = postRepository.findAllByStatusAndDateLessThanEqual(Post.Status.SCHEDULED, now);
		for (Post post : posts) {
			post.setStatus(Post.Status.PUBLISHED);
			postRepository.saveAndFlush(post);
		}

		if (!CollectionUtils.isEmpty(posts)) {
			cacheManager.getCache(WallRideCacheConfiguration.ARTICLE_CACHE).clear();
			cacheManager.getCache(WallRideCacheConfiguration.PAGE_CACHE).clear();
		}

		return posts;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Override
	public void updatePostViews() {
		LocalDateTime now = LocalDateTime.now();
		Set<JobExecution> jobExecutions = jobExplorer.findRunningJobExecutions("updatePostViewsJob");
		for (JobExecution jobExecution : jobExecutions) {
			LocalDateTime startTime = LocalDateTime.ofInstant(jobExecution.getStartTime().toInstant(), ZoneId.systemDefault());
			Duration d = Duration.between(now, startTime);
			if (Math.abs(d.toMinutes()) == 0) {
				logger.info("Skip processing because the job is running.");
				return;
			}
		}

		JobParameters params = new JobParametersBuilder()
				.addDate("now", Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
				.toJobParameters();
		try {
			jobLauncher.run(updatePostViewsJob, params);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updatePopularPosts(Language language, PopularPost.Type type, int maxRank) {
		logger.info("Start update of the popular posts");

		GoogleAnalytics googleAnalytics = language.getBlog().getGoogleAnalytics();
		if (googleAnalytics == null) {
			logger.info("Configuration of Google Analytics can not be found");
			return;
		}

		Analytics analytics = GoogleAnalyticsUtils.buildClient(googleAnalytics);

		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext, "org.springframework.web.servlet.FrameworkServlet.CONTEXT.guestServlet");
		if (context == null) {
			logger.info("GuestServlet is not ready yet");
			return;
		}

		final RequestMappingHandlerMapping mapping = context.getBean(RequestMappingHandlerMapping.class);

		Map<Post, Long> posts = new LinkedHashMap<>();

		int startIndex = 1;
		int currentRetry = 0;
		int totalResults = 0;

		do {
			try {
				LocalDate now = LocalDate.now();
				LocalDate startDate;
				switch (type) {
					case DAILY: startDate = now.minusDays(1); break;
					case WEEKLY: startDate = now.minusWeeks(1); break;
					case MONTHLY: startDate = now.minusMonths(1); break;
					default: throw new ServiceException();
				}

				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				Analytics.Data.Ga.Get get = analytics.data().ga()
						.get(googleAnalytics.getProfileId(), startDate.format(dateTimeFormatter), now.format(dateTimeFormatter), "ga:sessions")
						.setDimensions(String.format("ga:pagePath", googleAnalytics.getCustomDimensionIndex()))
						.setSort(String.format("-ga:sessions", googleAnalytics.getCustomDimensionIndex()))
						.setStartIndex(startIndex)
						.setMaxResults(GoogleAnalyticsUtils.MAX_RESULTS);
				if (language.getBlog().isMultiLanguage()) {
					get.setFilters("ga:pagePath=~/" + language.getLanguage() + "/");
				}

				logger.info(get.toString());
				final GaData gaData = get.execute();
				if (CollectionUtils.isEmpty(gaData.getRows())) {
					break;
				}

				for (List row : gaData.getRows()) {
					UriComponents uriComponents = UriComponentsBuilder.fromUriString((String) row.get(0)).build();

					MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
					request.setRequestURI(uriComponents.getPath());
					request.setQueryString(uriComponents.getQuery());
					MockHttpServletResponse response = new MockHttpServletResponse();

					BlogLanguageRewriteRule rewriteRule = new BlogLanguageRewriteRule(blogService);
					BlogLanguageRewriteMatch rewriteMatch = (BlogLanguageRewriteMatch) rewriteRule.matches(request, response);
					try {
						rewriteMatch.execute(request, response);
					} catch (ServletException e) {
						throw new ServiceException(e);
					} catch (IOException e) {
						throw new ServiceException(e);
					}

					request.setRequestURI(rewriteMatch.getMatchingUrl());

					HandlerExecutionChain handler;
					try {
						handler = mapping.getHandler(request);
					} catch (Exception e) {
						throw new ServiceException(e);
					}

					if (!(handler.getHandler() instanceof HandlerMethod)) {
						continue;
					}

					HandlerMethod method = (HandlerMethod) handler.getHandler();
					if (!method.getBeanType().equals(ArticleDescribeController.class) && !method.getBeanType().equals(PageDescribeController.class)) {
						continue;
					}

					// Last path mean code of post
					String code = uriComponents.getPathSegments().get(uriComponents.getPathSegments().size() - 1);
					Post post = postRepository.findOneByCodeAndLanguage(code, rewriteMatch.getLanguage().getLanguage());
					if (post == null) {
						logger.debug("Post not found [{}]", code);
						continue;
					}

					if (!posts.containsKey(post)) {
						posts.put(post, Long.parseLong((String) row.get(1)));
					}
					if (posts.size() >= maxRank) {
						break;
					}
				}

				if (posts.size() >= maxRank) {
					break;
				}

				startIndex += GoogleAnalyticsUtils.MAX_RESULTS;
				totalResults = gaData.getTotalResults();
			} catch (IOException e) {
				logger.warn("Failed to synchronize with Google Analytics", e);
				if (currentRetry >= GoogleAnalyticsUtils.MAX_RETRY) {
					throw new GoogleAnalyticsException(e);
				}

				currentRetry++;
				logger.info("{} ms to sleep...", GoogleAnalyticsUtils.RETRY_INTERVAL);
				try {
					Thread.sleep(GoogleAnalyticsUtils.RETRY_INTERVAL);
				} catch (InterruptedException ie) {
					throw new GoogleAnalyticsException(e);
				}
				logger.info("Retry for the {} time", currentRetry);
			}
		} while (startIndex <= totalResults);

		popularPostRepository.deleteByType(language.getLanguage(), type);

		int rank = 1;
		for (Map.Entry<Post, Long> entry : posts.entrySet()) {
			PopularPost popularPost = new PopularPost();
			popularPost.setLanguage(language.getLanguage());
			popularPost.setType(type);
			popularPost.setRank(rank);
			popularPost.setViews(entry.getValue());
			popularPost.setPost(entry.getKey());
			popularPostRepository.saveAndFlush(popularPost);
			rank++;
		}

		logger.info("Complete the update of popular posts");
	}
	@Override
	public Page<Post> getPosts(PostSearchRequest request) {
		Pageable pageable = new PageRequest(0, 10);
		return getPosts(request, pageable);
	}
	@Override
	public Page<Post> getPosts(PostSearchRequest request, Pageable pageable) {
//		return postRepository.search(request, pageable);
		return postRepository.findAll(pageable);
	}
	@Override
	public SortedSet<PopularPost> getPopularPosts(String language, PopularPost.Type type) {
		Specification<PopularPost> spec = (root, query, cb) -> {
			Join<PopularPost, Post> post = (Join<PopularPost, Post>) root.fetch(PopularPost_.post);
			post.fetch(Post_.cover);
			post.fetch(Post_.author);

			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.equal(root.get(PopularPost_.language), language));
			predicates.add(cb.equal(root.get(PopularPost_.type), type));
			predicates.add(cb.equal(post.get(Post_.status), Post.Status.PUBLISHED));
			return cb.and(predicates.toArray(new Predicate[0]));
		};
		return popularPostRepository.findAll(spec);
	}

	@Override
	public Post getPostById(long id, String language) {
		return postRepository.findOneByIdAndLanguage(id, language);
	}

	@Override
	public Page<Post> getPostsByTag(Tag tag, Pageable pageable) {
		return postRepository.findByTags(tag, pageable);
	}
}
