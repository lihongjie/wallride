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

package org.wallride.web.controller.guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.domain.Article;
import org.wallride.domain.Language;
import org.wallride.model.ArticleSearchRequest;
import org.wallride.service.ArticleService;
import org.wallride.service.TopicCategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Controller
@RequestMapping("/feed")
public class FeedController {

	private static final PageRequest DEFAULT_PAGE_REQUEST = new PageRequest(0, 50);

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private TopicCategoryService topicCategoryService;

	@RequestMapping("rss.xml")
	public String indexRss(
			Language language,
			Model model) {
		ArticleSearchRequest request = new ArticleSearchRequest()
				.withStatus(Post.Status.PUBLISHED)
				.withLanguage(language.getLanguage());
		Page<Article> articles = articleService.getArticles(request, DEFAULT_PAGE_REQUEST);
		model.addAttribute("articles", new TreeSet<>(articles.getContent()));
		return "rssFeedView";
	}

	@RequestMapping("atom.xml")
	public String indexAtom(
			Language language,
			Model model) {
		indexRss(language, model);
		return "atomFeedView";
	}

	@RequestMapping("category/{categoryCode}/rss.xml")
	public String categoryRss(
			@PathVariable String categoryCode,
			Language language,
			Model model) {
		Category category = topicCategoryService.getCategoryByCode(categoryCode, language.getLanguage());
		List<Long> categoryIds = new ArrayList<>();
		categoryIds.add(category.getId());

		ArticleSearchRequest request = new ArticleSearchRequest()
				.withStatus(Post.Status.PUBLISHED)
				.withLanguage(language.getLanguage())
				.withCategoryIds(categoryIds);

		Page<Article> articles = articleService.getArticles(request, DEFAULT_PAGE_REQUEST);
		model.addAttribute("articles", new TreeSet<>(articles.getContent()));
		return "rssFeedView";
	}

	@RequestMapping("category/{categoryCode}/atom.xml")
	public String categoryAtom(
			@PathVariable String categoryCode,
			Language language,
			Model model) {
		categoryRss(categoryCode, language, model);
		return "atomFeedView";
	}
}
