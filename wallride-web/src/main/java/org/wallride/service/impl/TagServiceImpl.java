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

import com.github.lihongjie.core.base.util.Args;
import com.github.lihongjie.core.base.util.SequenceUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.wallride.domain.Tag;
import org.wallride.exception.DuplicateNameException;
import org.wallride.mapper.TagMapper;
import org.wallride.model.*;
import org.wallride.service.TagService;
import org.wallride.support.AuthorizedUser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagServiceImpl implements TagService {

    private static Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private TagMapper tagMapper;

    @Override
    public void createTag(TagRequest request, AuthorizedUser authorizedUser) {

        Tag duplicate = tagMapper.findOneByNameAndLanguage(buildNameAndLanguageParams(request.getName(), request.getLanguage()));
        if (duplicate != null) {
            throw new DuplicateNameException(request.getName());
        }
        Tag tag = new Tag();
        tag.setId(SequenceUtils.nexSeqId());
        LocalDateTime now = LocalDateTime.now();
        tag.setName(request.getName());
        tag.setLanguage(request.getLanguage());
        tag.setCreatedStamp(now);
        tag.setCreatedTxStamp(now);
        tag.setLastUpdatedStamp(now);
        tag.setLastUpdatedTxStamp(now);
        tagMapper.insertSelective(tag);
    }

    @Override
    public void updateTag(String id, TagRequest request, AuthorizedUser authorizedUser) {

        Args.notNull(id, "id");
        Tag tag = tagMapper.findOneByIdAndLanguage(buildIdAndLanguageParams(id, request.getLanguage()));
        LocalDateTime now = LocalDateTime.now();
        if (!ObjectUtils.nullSafeEquals(tag.getName(), request.getName())) {
            Tag duplicate = tagMapper.findOneByNameAndLanguage(buildNameAndLanguageParams(request.getName(), request.getLanguage()));
            if (duplicate != null) {
                throw new DuplicateNameException(request.getName());
            }
        }
        tag.setName(request.getName());
        tag.setLanguage(request.getLanguage());
        tag.setLastUpdatedTxStamp(now);
        tagMapper.updateByPrimaryKey(tag);

    }

    @Override
    public void mergeTags(TagMergeRequest request, AuthorizedUser authorizedUser) {
//        // Get all articles that have tag for merging
//        ArticleSearchRequest searchRequest = new ArticleSearchRequest()
//                .withTagIds(request.getIds());
////		Page<Article> articles = articleRepository.search(searchRequest);
//        Page<Article> articles = null;
//
//        // Delete old tag after merging
//        for (long id : request.getIds()) {
//            tagRepository.delete(id);
//        }
//
//        // Create a new Tag
//        TagRequest createRequest = new TagRequest.Builder()
//                .name(request.getName())
//                .language(request.getLanguage())
//                .build();
//        Tag mergedTag = createTag(createRequest, authorizedUser);
//
//        for (Article article : articles) {
//            article.getTags().add(mergedTag);
//            articleRepository.saveAndFlush(article);
//        }
//
//        return mergedTag;
    }

    @Override
    public void bulkDeleteTag(TagBulkDeleteRequest bulkDeleteRequest) {

        tagMapper.deleteInBatch(bulkDeleteRequest.getIds());
    }

    @Override
    public Tag getTagById(String id, String language) {

        return tagMapper.findOneByIdAndLanguage(buildIdAndLanguageParams(id, language));
    }

    @Override
    public Tag getTagByName(String name, String language) {

        return tagMapper.findOneByIdAndLanguage(buildNameAndLanguageParams(name, language));
    }

    @Override
    public List<Tag> getTags(String language) {

        return tagMapper.findAllByLanguage(language);
    }

    @Override
    public PageInfo<Tag> getTags(TagExample example) {

        PageHelper.startPage(example.getPageNum(), example.getPageSize());
        List<Tag> tags = tagMapper.selectByExample(example);
        return new PageInfo<>(tags);
    }

    private Map<String, Object> buildIdAndLanguageParams(String id, String language) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("language", language);
        return params;
    }

    private Map<String, Object> buildNameAndLanguageParams(String name, String language) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("language", language);
        return params;
    }

}
