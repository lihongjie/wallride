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

package org.wallride.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wallride.domain.Article;
import org.wallride.domain.Post;
import org.wallride.domain.Tag;
import org.wallride.model.*;
import org.wallride.support.AuthorizedUser;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public interface ArticleService {

	Article createArticle(ArticleCreateRequest request, Post.Status status, AuthorizedUser authorizedUser) throws ParseException;

	Article saveArticleAsDraft(ArticleUpdateRequest request, AuthorizedUser authorizedUser) throws ParseException;

	Article saveArticleAsPublished(ArticleUpdateRequest request, AuthorizedUser authorizedUser);

	Article saveArticleAsUnpublished(ArticleUpdateRequest request, AuthorizedUser authorizedUser);

	Article saveArticle(ArticleUpdateRequest request, AuthorizedUser authorizedUser);

	void deleteArticle(ArticleBulkDeleteRequest request);

	List<Article> bulkPublishArticle(ArticleBulkPublishRequest request, AuthorizedUser authorizedUser);

	List<Article> bulkUnpublishArticle(ArticleBulkUnpublishRequest request, AuthorizedUser authorizedUser);

	Page<Article> getArticles(ArticleSearchRequest request);

	Page<Article> getArticles(ArticleSearchRequest request, Pageable pageable);

	List<Article> getArticles(Collection<Long> ids);

	SortedSet<Article> getArticlesByCategoryCode(String language, String code, Post.Status status);

	SortedSet<Article> getArticlesByCategoryCode(String language, String code, Post.Status status, int size);

	SortedSet<Article> getLatestArticles(String language, Post.Status status, int size);

	Article getArticleById(Long id);

	Article getArticleById(Long id, String language);

	Article getArticleByCode(String code, String language);

	Article getDraftById(Long id);

	Long countArticles(String language);

	Long countArticlesByStatus(Post.Status status, String language);

	Map<Long, Long> countArticlesByAuthorIdGrouped(Post.Status status, String language);

	Map<Long, Long> countArticlesByCategoryIdGrouped(Post.Status status, String language);

	Map<Long, Long> countArticlesByTagIdGrouped(Post.Status status, String language);

	Article getNextArticle(Long id);

	Article getPrevArticle(Long id);

	List<ArticleArchiveResponse> articleArchive(Long id);

	List<Tag> tagsArchive(Long id);
}
