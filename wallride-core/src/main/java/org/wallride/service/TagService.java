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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.wallride.autoconfigure.WallRideCacheConfiguration;
import org.wallride.domain.Article;
import org.wallride.domain.Tag;
import org.wallride.exception.DuplicateNameException;
import org.wallride.model.*;
import org.wallride.repository.ArticleRepository;
import org.wallride.repository.TagRepository;
import org.wallride.support.AuthorizedUser;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class TagService {

    private static Logger logger = LoggerFactory.getLogger(TagService.class);

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public Tag createTag(TagCreateRequest request, AuthorizedUser authorizedUser) {
        Tag duplicate = tagRepository.findOneByNameAndLanguage(request.getName(), request.getLanguage());
        if (duplicate != null) {
            throw new DuplicateNameException(request.getName());
        }
        Tag tag = new Tag();
        LocalDateTime now = LocalDateTime.now();
        tag.setName(request.getName());
        tag.setLanguage(request.getLanguage());
        tag.setCreatedAt(now);
        tag.setCreatedBy(authorizedUser.toString());
        tag.setUpdatedAt(now);
        tag.setUpdatedBy(authorizedUser.toString());
        return tagRepository.saveAndFlush(tag);
    }

    public Tag updateTag(TagUpdateRequest request, AuthorizedUser authorizedUser) {
        Tag tag = tagRepository.findOneForUpdateByIdAndLanguage(request.getId(), request.getLanguage());
        LocalDateTime now = LocalDateTime.now();

        if (!ObjectUtils.nullSafeEquals(tag.getName(), request.getName())) {
            Tag duplicate = tagRepository.findOneByNameAndLanguage(request.getName(), request.getLanguage());
            if (duplicate != null) {
                throw new DuplicateNameException(request.getName());
            }
        }

        tag.setName(request.getName());
        tag.setLanguage(request.getLanguage());

        tag.setUpdatedAt(now);
        tag.setUpdatedBy(authorizedUser.toString());

        return tagRepository.saveAndFlush(tag);
    }

    @CacheEvict(value = {WallRideCacheConfiguration.ARTICLE_CACHE, WallRideCacheConfiguration.PAGE_CACHE}, allEntries = true)
    public Tag mergeTags(TagMergeRequest request, AuthorizedUser authorizedUser) {
        // Get all articles that have tag for merging
        ArticleSearchRequest searchRequest = new ArticleSearchRequest()
                .withTagIds(request.getIds());
//		Page<Article> articles = articleRepository.search(searchRequest);
        Page<Article> articles = null;

        // Delete old tag after merging
        for (long id : request.getIds()) {
            tagRepository.delete(id);
        }

        // Create a new Tag
        TagCreateRequest createRequest = new TagCreateRequest.Builder()
                .name(request.getName())
                .language(request.getLanguage())
                .build();
        Tag mergedTag = createTag(createRequest, authorizedUser);

        for (Article article : articles) {
            article.getTags().add(mergedTag);
            articleRepository.saveAndFlush(article);
        }

        return mergedTag;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Tag> bulkDeleteTag(TagBulkDeleteRequest bulkDeleteRequest) {

        List<Tag> tags = tagRepository.findAll(bulkDeleteRequest.getIds());
        tagRepository.deleteInBatch(tags);
        return tags;
    }

    public Tag getTagById(long id, String language) {
        return tagRepository.findOneByIdAndLanguage(id, language);
    }

    public Tag getTagByName(String name, String language) {
        return tagRepository.findOneByNameAndLanguage(name, language);
    }

    public List<Tag> getTags(String language) {
        return tagRepository.findAllByLanguage(language);
    }

    public Page<Tag> getTags(TagSearchRequest request) {
        Pageable pageable = new PageRequest(0, 10);
        return getTags(request, pageable);
    }

    public Page<Tag> getTags(TagSearchRequest request, Pageable pageable) {
//		return tagRepository.search(request, pageable);
        return tagRepository.findAll(pageable);
    }
}
