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

import com.github.lihongjie.core.base.util.SequenceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.domain.*;
import org.wallride.mapper.*;
import org.wallride.model.*;
import org.wallride.repository.*;
import org.wallride.service.ArticleService;
import org.wallride.support.AuthorizedUser;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Transactional
    public Article saveArticle(ArticleRequest articleRequest) {

        String articleId = SequenceUtils.nexSeqId();
        Article record = articleRequest.buildArticle();
        record.setId(articleId);
        LocalDateTime now = LocalDateTime.now();
        record.setCreatedStamp(now);
        record.setCreatedTxStamp(now);
        record.setLastUpdatedStamp(now);
        record.setLastUpdatedTxStamp(now);
        articleMapper.insertSelective(record);
        Set<String> tagIds = articleRequest.getTags();
        List<ArticleTag> articleTags = new ArrayList<>();
        for (String id : tagIds) {
            articleTags.add(new ArticleTag(SequenceUtils.nexSeqId(), articleId, id));
        }
        articleTagMapper.insertInBatch(articleTags);
        return record;
    }

    @Override
    public Article saveArticleAsDraft(ArticleRequest articleRequest, AuthorizedUser authorizedUser) {

        articleRequest.setStatus(Article.Status.DRAFT.name());
        articleRequest.setAuthorId(authorizedUser.getId());
        articleRequest.setAuthorName(authorizedUser.getPersonalName());
        return saveArticle(articleRequest);
    }

    @Override
    public Article saveArticleAsPublished(ArticleRequest articleRequest, AuthorizedUser authorizedUser) {

        articleRequest.setStatus(Article.Status.PUBLISHED.name());
        articleRequest.setAuthorId(authorizedUser.getId());
        articleRequest.setAuthorName(authorizedUser.getPersonalName());
        return saveArticle(articleRequest);
    }

    @Override
    public void updateArticleAsDraft(ArticleRequest articleRequest, String articleId) {

        Article article = articleRequest.buildArticle();
        article.setId(articleId);
        article.setStatus(Article.Status.DRAFT.name());
        article.setLastUpdatedStamp(LocalDateTime.now());
        articleMapper.updateByPrimaryKey(article);

    }

    @Override
    public void updateArticleAsPublished(ArticleRequest articleRequest, String articleId) {

        Article article = articleRequest.buildArticle();
        article.setId(articleId);
        article.setStatus(Article.Status.PUBLISHED.name());
        article.setLastUpdatedStamp(LocalDateTime.now());
        articleMapper.updateByPrimaryKey(article);
    }

    @Override
    public void deleteArticle(ArticlePublishRequest request) {

        articleMapper.deleteInBatch(request.getIds());
    }

    @Override
    @Transactional
    public void bulkPublishArticle(ArticlePublishRequest request) {

        List<String> ids = request.getIds();
        for (String id : ids) {
            Article article = new Article();
            article.setId(id);
            article.setLanguage(request.getLanguage());
            article.setLastUpdatedStamp(request.getDate());
            article.setStatus(Article.Status.PUBLISHED.name());
            articleMapper.updateByPrimaryKey(article);
        }

    }

    @Override
    @Transactional
    public void bulkArchiveArticle(ArticleBulkArchiveRequest request) {

        List<String> ids = request.getIds();
        for (String id : ids) {
            Article article = new Article();
            article.setId(id);
            article.setLanguage(request.getLanguage());
            article.setLastUpdatedStamp(request.getDate());
            article.setStatus(Article.Status.ARCHIVED.name());
            articleMapper.updateByPrimaryKey(article);
        }
    }

    @Override
	@Transactional
    public PageInfo<Article> getArticles(ArticleExample example) {

        PageHelper.startPage(example.getPageNum(), example.getPageSize());
        List<Article> articles = articleMapper.selectByExample(example);
        return new PageInfo<>(articles);
    }


    @Override
    public Article getArticleById(String id, String language) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("language", language);
        return articleMapper.findOneByIdAndLanguage(params);
    }

    @Override
    public Article getArticleByCode(String code, String language) {

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("langugae", language);
        return articleMapper.findOneByCodeAndLanguage(params);
    }

    @Override
    public Article getNextArticle(String id, String language) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("language", language);
        return articleMapper.findTopByIdIsAfterOrderByIdAsc(params);
    }

    @Override
    public Article getPrevArticle(String id, String language) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("language", language);
        return articleMapper.findTopByIdIsBeforeOrderByIdDesc(params);
    }

    @Override
    public List<ArticleArchiveResponse> archiveArticles(String articleId, String language) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", articleId);
        params.put("language", language);
        return articleMapper.archiveArticles(params);
    }

    @Override
    public List<Tag> tagsArchive(String articleId) {

        return tagMapper.findTagsByArticleId(articleId);
    }

}
