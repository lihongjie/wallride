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

import com.github.pagehelper.PageInfo;
import org.wallride.domain.Article;
import org.wallride.domain.Tag;
import org.wallride.model.*;
import org.wallride.support.AuthorizedUser;

import java.util.List;

public interface ArticleService {

	Article saveArticleAsDraft(ArticleRequest request, AuthorizedUser authorizedUser);

	Article saveArticleAsPublished(ArticleRequest request, AuthorizedUser authorizedUser);

	void updateArticleAsDraft(ArticleRequest request, String id);

	void updateArticleAsPublished(ArticleRequest request, String id);

	void deleteArticle(ArticlePublishRequest request);

	void bulkPublishArticle(ArticlePublishRequest request);

	void bulkArchiveArticle(ArticleBulkArchiveRequest request);

	PageInfo<Article> getArticles(ArticleExample example);

	Article getArticleById(String id, String language);

	Article getArticleByCode(String code, String language);

	Article getNextArticle(String id, String language);

	Article getPrevArticle(String id, String language);

	List<ArticleArchiveResponse> archiveArticles(String id, String language);

	List<Tag> tagsArchive(String id);
}
