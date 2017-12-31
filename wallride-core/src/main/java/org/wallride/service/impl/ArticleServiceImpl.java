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

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wallride.autoconfigure.WallRideProperties;
import org.wallride.domain.*;
import org.wallride.exception.DuplicateCodeException;
import org.wallride.exception.EmptyCodeException;
import org.wallride.exception.ServiceException;
import org.wallride.model.*;
import org.wallride.repository.*;
import org.wallride.service.ArticleService;
import org.wallride.support.AuthorizedUser;
import org.wallride.support.CodeFormatter;
import org.wallride.web.controller.admin.article.CustomFieldValueEditForm;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ArticleServiceImpl implements ArticleService {

    private static Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private WallRideProperties wallRideProperties;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CustomFieldRepository customFieldRepository;

    @Override
    public Article createArticle(ArticleCreateRequest request, Post.Status status, AuthorizedUser authorizedUser) throws ParseException {
        LocalDateTime now = LocalDateTime.now();

        String code = request.getCode();
        if (code == null) {

            code = new CodeFormatter().parse(request.getTitle(), LocaleContextHolder.getLocale());

        }
        if (!StringUtils.hasText(code)) {
            if (!status.equals(Post.Status.DRAFT)) {
                throw new EmptyCodeException();
            }
        }

        if (!status.equals(Post.Status.DRAFT)) {
            Post duplicate = postRepository.findOneByCodeAndLanguage(code, request.getLanguage());
            if (duplicate != null) {
                throw new DuplicateCodeException(code);
            }
        }

        Article article = new Article();

        if (!status.equals(Post.Status.DRAFT)) {
            article.setCode(code);
            article.setDraftedCode(null);
        } else {
            article.setCode(null);
            article.setDraftedCode(code);
        }

        Media cover = null;
        if (request.getCoverId() != null) {
            cover = mediaRepository.findOne(request.getCoverId());
        }
        article.setCover(cover);
        article.setTitle(request.getTitle());
        article.setBody(request.getBody());
        article.setAuthor(authorizedUser);

        LocalDateTime date = request.getDate();
        if (Post.Status.PUBLISHED.equals(status)) {
            if (date == null) {
                date = now;
            }
        }
        article.setDate(date);
        article.setStatus(status);
        article.setLanguage(request.getLanguage());

        article.getCategories().clear();
        for (long categoryId : request.getCategoryIds()) {
            article.getCategories().add(categoryRepository.findOne(categoryId));
        }
        article.getTags().clear();
        Set<String> tagNames = StringUtils.commaDelimitedListToSet(request.getTags());
        if (!CollectionUtils.isEmpty(tagNames)) {
            for (String tagName : tagNames) {
                Tag tag = tagRepository.findOneForUpdateByNameAndLanguage(tagName, request.getLanguage());
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(tagName);
                    tag.setLanguage(request.getLanguage());
                    article.setCreatedAt(now);
                    article.setCreatedBy(authorizedUser.toString());
                    article.setUpdatedAt(now);
                    article.setUpdatedBy(authorizedUser.toString());
                    tag = tagRepository.saveAndFlush(tag);
                }
                article.getTags().add(tag);
            }
        }

        article.getRelatedPosts().clear();
        Set<Post> relatedPosts = new HashSet<>();
        for (long relatedId : request.getRelatedPostIds()) {
            relatedPosts.add(postRepository.findOne(relatedId));
        }
        article.setRelatedToPosts(relatedPosts);

        Seo seo = new Seo();
        seo.setTitle(request.getSeoTitle());
        seo.setDescription(request.getSeoDescription());
        seo.setKeywords(request.getSeoKeywords());
        article.setSeo(seo);

        List<Media> medias = new ArrayList<>();
        if (StringUtils.hasText(request.getBody())) {
            String mediaUrlPrefix = wallRideProperties.getMediaUrlPrefix();
            Pattern mediaUrlPattern = Pattern.compile(String.format("%s([0-9a-zA-Z\\-]+)", mediaUrlPrefix));
            Matcher mediaUrlMatcher = mediaUrlPattern.matcher(request.getBody());
            while (mediaUrlMatcher.find()) {
                Media media = mediaRepository.findOneById(mediaUrlMatcher.group(1));
                medias.add(media);
            }
        }
        article.setMedias(medias);

        article.setCreatedAt(now);
        article.setCreatedBy(authorizedUser.toString());
        article.setUpdatedAt(now);
        article.setUpdatedBy(authorizedUser.toString());

        article.getCustomFieldValues().clear();
        if (!CollectionUtils.isEmpty(request.getCustomFieldValues())) {
            for (CustomFieldValueEditForm valueForm : request.getCustomFieldValues()) {
                CustomFieldValue value = new CustomFieldValue();
                value.setCustomField(customFieldRepository.findOne(valueForm.getCustomFieldId()));
                value.setPost(article);
                if (valueForm.getFieldType().equals(CustomField.FieldType.CHECKBOX)) {
                    if (!ArrayUtils.isEmpty(valueForm.getTextValues())) {
                        value.setTextValue(String.join(",", valueForm.getTextValues()));
                    } else {
                        value.setTextValue(null);
                    }
                } else {
                    value.setTextValue(valueForm.getTextValue());
                }
                value.setStringValue(valueForm.getStringValue());
                value.setNumberValue(valueForm.getNumberValue());
                value.setDateValue(valueForm.getDateValue());
                value.setDatetimeValue(valueForm.getDatetimeValue());
                if (!value.isEmpty()) {
                    article.getCustomFieldValues().add(value);
                }
            }
        }
        return articleRepository.save(article);
    }

    @Override
    public Article saveArticleAsDraft(ArticleUpdateRequest request, AuthorizedUser authorizedUser) throws ParseException {
        Article article = articleRepository.findOneByIdAndLanguage(request.getId(), request.getLanguage());
        if (!article.getStatus().equals(Post.Status.DRAFT)) {
            Article draft = articleRepository.findOne(ArticleSpecifications.draft(article));
            if (draft == null) {
                ArticleCreateRequest createRequest = new ArticleCreateRequest.Builder()
                        .code(request.getCode())
                        .coverId(request.getCoverId())
                        .title(request.getTitle())
                        .body(request.getBody())
                        .authorId(request.getAuthorId())
                        .date(request.getDate())
                        .categoryIds(request.getCategoryIds())
                        .tags(request.getTags())
                        .seoTitle(request.getSeoTitle())
                        .seoDescription(request.getSeoDescription())
                        .seoKeywords(request.getSeoKeywords())
                        .customFieldValues(new ArrayList<>(request.getCustomFieldValues()))
                        .language(request.getLanguage())
                        .build();
                draft = createArticle(createRequest, Post.Status.DRAFT, authorizedUser);
                draft.setDrafted(article);
                return articleRepository.save(draft);
            } else {
                ArticleUpdateRequest updateRequest = new ArticleUpdateRequest.Builder()
                        .id(draft.getId())
                        .code(request.getCode())
                        .coverId(request.getCoverId())
                        .title(request.getTitle())
                        .body(request.getBody())
                        .authorId(request.getAuthorId())
                        .date(request.getDate())
                        .categoryIds(request.getCategoryIds())
                        .tags(request.getTags())
                        .seoTitle(request.getSeoTitle())
                        .seoDescription(request.getSeoDescription())
                        .seoKeywords(request.getSeoKeywords())
                        .customFieldValues(request.getCustomFieldValues())
                        .language(request.getLanguage())
                        .build();
                return saveArticle(updateRequest, authorizedUser);
            }
        } else {
            return saveArticle(request, authorizedUser);
        }
    }

    @Override
    public Article saveArticleAsPublished(ArticleUpdateRequest request, AuthorizedUser authorizedUser) {

        Article article = articleRepository.findOneByIdAndLanguage(request.getId(), request.getLanguage());
        publishArticle(article);
        return saveArticle(request, authorizedUser);
    }

    @Override
    public Article saveArticleAsUnpublished(ArticleUpdateRequest request, AuthorizedUser authorizedUser) {
        Article article = articleRepository.findOneByIdAndLanguage(request.getId(), request.getLanguage());
        unpublishArticle(article);
        return saveArticle(request, authorizedUser);
    }

    private Article unpublishArticle(Article article) {
        Article deleteTarget = getDraftById(article.getId());
        if (deleteTarget != null) {
            articleRepository.delete(deleteTarget);
        }
        article.setDrafted(null);
        article.setStatus(Post.Status.DRAFT);
        Article unpublished = articleRepository.save(article);
        return unpublished;
    }

    @Override
    public Article saveArticle(ArticleUpdateRequest request, AuthorizedUser authorizedUser) {
        Article article = articleRepository.findOneByIdAndLanguage(request.getId(), request.getLanguage());
        LocalDateTime now = LocalDateTime.now();

        String code = request.getCode();
        if (code == null) {
            try {
                code = new CodeFormatter().parse(request.getTitle(), LocaleContextHolder.getLocale());
            } catch (ParseException e) {
                throw new ServiceException(e);
            }
        }
        if (!StringUtils.hasText(code)) {
            if (!article.getStatus().equals(Post.Status.DRAFT)) {
                throw new EmptyCodeException();
            }
        }
        if (!article.getStatus().equals(Post.Status.DRAFT)) {
            Post duplicate = postRepository.findOneByCodeAndLanguage(code, request.getLanguage());
            if (duplicate != null && !duplicate.equals(article)) {
                throw new DuplicateCodeException(code);
            }
        }

        if (!article.getStatus().equals(Post.Status.DRAFT)) {
            article.setCode(code);
            article.setDraftedCode(null);
        } else {
            article.setCode(null);
            article.setDraftedCode(code);
        }

        Media cover = null;
        if (request.getCoverId() != null) {
            cover = mediaRepository.findOne(request.getCoverId());
        }
        article.setCover(cover);
        article.setTitle(request.getTitle());
        article.setBody(request.getBody());
        LocalDateTime date = request.getDate();
        if (!Post.Status.DRAFT.equals(article.getStatus())) {
            if (date == null) {
                date = now;
            } else if (date.isAfter(now)) {
                article.setStatus(Post.Status.SCHEDULED);
            } else {
                article.setStatus(Post.Status.PUBLISHED);
            }
        }
        article.setDate(date);
        article.setLanguage(request.getLanguage());

        article.getCategories().clear();
        for (long categoryId : request.getCategoryIds()) {
            Category category = categoryRepository.findOne(categoryId);
            article.getCategories().add(category);
        }

        article.getTags().clear();
        Set<String> tagNames = StringUtils.commaDelimitedListToSet(request.getTags());
        if (!CollectionUtils.isEmpty(tagNames)) {
            for (String tagName : tagNames) {
                Tag tag = tagRepository.findOneForUpdateByNameAndLanguage(tagName, request.getLanguage());
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(tagName);
                    tag.setLanguage(request.getLanguage());
                    article.setCreatedAt(now);
                    article.setCreatedBy(authorizedUser.toString());
                    article.setUpdatedAt(now);
                    article.setUpdatedBy(authorizedUser.toString());
                    tag = tagRepository.saveAndFlush(tag);
                }
                article.getTags().add(tag);
            }
        }

        article.getRelatedPosts().clear();
        Set<Post> relatedPosts = new HashSet<>();
        for (long relatedId : request.getRelatedPostIds()) {
            Post post = postRepository.findOne(relatedId);
            relatedPosts.add(post);
        }
        article.setRelatedToPosts(relatedPosts);

        Seo seo = new Seo();
        seo.setTitle(request.getSeoTitle());
        seo.setDescription(request.getSeoDescription());
        seo.setKeywords(request.getSeoKeywords());
        article.setSeo(seo);

        List<Media> medias = new ArrayList<>();
        if (StringUtils.hasText(request.getBody())) {
//			Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
            String mediaUrlPrefix = wallRideProperties.getMediaUrlPrefix();
            Pattern mediaUrlPattern = Pattern.compile(String.format("%s([0-9a-zA-Z\\-]+)", mediaUrlPrefix));
            Matcher mediaUrlMatcher = mediaUrlPattern.matcher(request.getBody());
            while (mediaUrlMatcher.find()) {
                Media media = mediaRepository.findOneById(mediaUrlMatcher.group(1));
                medias.add(media);
            }
        }
        article.setMedias(medias);

        article.setUpdatedAt(now);
        article.setUpdatedBy(authorizedUser.toString());

        Map<CustomField, CustomFieldValue> valueMap = new LinkedHashMap<>();
        for (CustomFieldValue value : article.getCustomFieldValues()) {
            valueMap.put(value.getCustomField(), value);
        }

        article.getCustomFieldValues().clear();
        if (!CollectionUtils.isEmpty(request.getCustomFieldValues())) {
            for (CustomFieldValueEditForm valueForm : request.getCustomFieldValues()) {
                CustomField customField = customFieldRepository.findOne(valueForm.getCustomFieldId());
                CustomFieldValue value = valueMap.get(customField);
                if (value == null) {
                    value = new CustomFieldValue();
                }
                value.setCustomField(customField);
                value.setPost(article);
                if (valueForm.getFieldType().equals(CustomField.FieldType.CHECKBOX)) {
                    if (!ArrayUtils.isEmpty(valueForm.getTextValues())) {
                        value.setTextValue(String.join(",", valueForm.getTextValues()));
                    } else {
                        value.setTextValue(null);
                    }
                } else {
                    value.setTextValue(valueForm.getTextValue());
                }
                value.setStringValue(valueForm.getStringValue());
                value.setNumberValue(valueForm.getNumberValue());
                value.setDateValue(valueForm.getDateValue());
                value.setDatetimeValue(valueForm.getDatetimeValue());
                if (!value.isEmpty()) {
                    article.getCustomFieldValues().add(value);
                }
            }
        }

        return articleRepository.save(article);
    }

    @Override
    public void deleteArticle(ArticleBulkDeleteRequest request) {

        List<Article> articles = articleRepository.findAll(request.getIds());
        articleRepository.delete(articles);
    }

    @Override
    @Transactional
    public List<Article> bulkPublishArticle(ArticleBulkPublishRequest request, AuthorizedUser authorizedUser) {

        List<Long> ids = request.getIds();
        List<Article> selectedArticles = articleRepository.findAll(ids);
        for (Article article : selectedArticles) {
            LocalDateTime now = LocalDateTime.now();
            article.setStatus(Post.Status.PUBLISHED);
            article.setDate(now);
            article.setUpdatedAt(now);
            article.setUpdatedBy(authorizedUser.toString());
        }
        return articleRepository.save(selectedArticles);
//		for (long id : request.getIds()) {
//			Article article = articleRepository.findOneByIdAndLanguage(id, request.getLanguage());
//			if (article.getStatus() != Post.Status.DRAFT && request.getDate() == null) {
//				continue;
//			}
//			LocalDateTime now = LocalDateTime.now();
//			LocalDateTime date = article.getDate();
//			if (request.getDate() != null) {
//				date = request.getDate();
//			}
//			if (date == null) {
//				date = now;
//			}
//			article.setDate(date);
//			article.setUpdatedAt(now);
//			article.setUpdatedBy(authorizedUser.toString());
//
//			article = publishArticle(article);
//
//			if (article.getDate().isAfter(now)) {
//				article.setStatus(Post.Status.SCHEDULED);
//			} else {
//				article.setStatus(Post.Status.PUBLISHED);
//			}
//			article = articleRepository.saveAndFlush(article);
//
//			articles.add(article);
//		}
//
//		return articles;
    }

    @Override
    public List<Article> bulkUnpublishArticle(ArticleBulkUnpublishRequest request, AuthorizedUser authorizedUser) {
        List<Long> ids = request.getIds();
        List<Article> selectedArticles = articleRepository.findAll(ids);
        for (Article article : selectedArticles) {
            LocalDateTime now = LocalDateTime.now();
            article.setStatus(Post.Status.DRAFT);
            article.setDate(null);
            article.setUpdatedAt(now);
            article.setUpdatedBy(authorizedUser.toString());
        }
        return articleRepository.save(selectedArticles);
    }

    @Override
    public Page<Article> getArticles(ArticleSearchRequest request) {
        Pageable pageable = new PageRequest(0, 10);
        return getArticles(request, pageable);
    }

    @Override
//	@Transactional
    public Page<Article> getArticles(ArticleSearchRequest request, Pageable pageable) {
//		return articleRepository.search(request, pageable);
        Page<Article> articles = articleRepository.findAll(pageable);
        int size = articles.getContent().size();
        return articles;
    }

    @Override
    public List<Article> getArticles(Collection<Long> ids) {
        Set<Article> results = new LinkedHashSet<>(articleRepository.findAllByIdIn(ids));
        List<Article> articles = new ArrayList<>();
        for (long id : ids) {
            for (Article article : results) {
                if (id == article.getId()) {
                    articles.add(article);
                    break;
                }
            }
        }
        return articles;
    }

    @Override
    public SortedSet<Article> getArticlesByCategoryCode(String language, String code, Post.Status status) {
        return getArticlesByCategoryCode(language, code, status, 10);
    }

    @Override
    public SortedSet<Article> getArticlesByCategoryCode(String language, String code, Post.Status status, int size) {
        ArticleSearchRequest request = new ArticleSearchRequest()
                .withLanguage(language)
                .withCategoryCodes(code)
                .withStatus(status);

        Pageable pageable = new PageRequest(0, size);
        Page<Article> page = articleRepository.findAll(ArticleSpecifications.searchRequestSpecification(request), pageable);
        return new TreeSet<>(page.getContent());

    }

    @Override
    public SortedSet<Article> getLatestArticles(String language, Post.Status status, int size) {
        ArticleSearchRequest request = new ArticleSearchRequest()
                .withLanguage(language)
                .withStatus(status);

        Pageable pageable = new PageRequest(0, size);
        Page<Article> page = articleRepository.findAll(ArticleSpecifications.searchRequestSpecification(request), pageable);
        return new TreeSet<>(page.getContent());
    }

    @Override
    public Article getArticleById(Long id) {
        return null;
    }

    @Override
    public Article getArticleById(Long id, String language) {
        return null;
    }

    @Override
    public Article getArticleByCode(String code, String language) {
        Article article = articleRepository.findOneByCodeAndLanguage(code, language);
        int size = article.getComments().size();
        return article;
    }

    @Override
    public Article getDraftById(Long id) {
        return null;
    }

    @Override
    public Long countArticles(String language) {
        return articleRepository.count(language);
    }

    public Long countArticlesByStatus(Post.Status status, String language) {
        return articleRepository.countByStatus(status, language);
    }

    @Override
    public Map<Long, Long> countArticlesByAuthorIdGrouped(Post.Status status, String language) {
        List<Map<String, Object>> results = articleRepository.countByAuthorIdGrouped(status, language);
        Map<Long, Long> counts = new HashMap<>();
        for (Map<String, Object> row : results) {
            counts.put((Long) row.get("userId"), (Long) row.get("count"));
        }
        return counts;
    }

    @Override
    public Map<Long, Long> countArticlesByCategoryIdGrouped(Post.Status status, String language) {
        List<Map<String, Object>> results = articleRepository.countByCategoryIdGrouped(status, language);
        Map<Long, Long> counts = new HashMap<>();
        for (Map<String, Object> row : results) {
            counts.put((Long) row.get("categoryId"), (Long) row.get("count"));
        }
        return counts;
    }

    @Override
    public Map<Long, Long> countArticlesByTagIdGrouped(Post.Status status, String language) {
        List<Map<String, Object>> results = articleRepository.countByTagIdGrouped(status, language);
        Map<Long, Long> counts = new HashMap<>();
        for (Map<String, Object> row : results) {
            counts.put((Long) row.get("tagId"), (Long) row.get("count"));
        }
        return counts;
    }

    @Override
    public Article getNextArticle(Long id) {

        return articleRepository.findTopByIdIsAfterOrderByIdAsc(id);
    }

    @Override
    public Article getPrevArticle(Long id) {

        return articleRepository.findTopByIdIsBeforeOrderByIdDesc(id);
    }

    @Override
    public List<ArticleArchiveResponse> articleArchive(Long id) {
        Article article = articleRepository.getOne(id);
        return articleRepository.articleArchive(article.getAuthor().getId());
    }

    private Article publishArticle(Article article) {
        Article deleteTarget = getDraftById(article.getId());
        if (deleteTarget != null) {
            articleRepository.delete(deleteTarget);
        }
        article.setDrafted(null);
        article.setStatus(Post.Status.PUBLISHED);
        Article published = articleRepository.save(article);
        return published;
    }

}
