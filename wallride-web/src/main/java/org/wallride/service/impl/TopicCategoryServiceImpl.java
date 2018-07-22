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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wallride.domain.TopicCategory;
import org.wallride.mapper.TopicCategoryMapper;
import org.wallride.model.TopicCategoryExample;
import org.wallride.model.TopicCategoryRequest;
import org.wallride.service.TopicCategoryService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TopicCategoryServiceImpl implements TopicCategoryService {

	@Autowired
	private TopicCategoryMapper topicCategoryMapper;

	@Override
	public PageInfo<TopicCategory> getTopicCategories(TopicCategoryExample example) {

		PageHelper.startPage(example.getPageNum(), example.getPageSize());
		List<TopicCategory> topicCategories = topicCategoryMapper.selectByExample(example);
		return new PageInfo<>(topicCategories);
	}

	@Override
	public void createTopicCategory(TopicCategoryRequest request) {

		TopicCategory topicCategory = request.buildTopicCategory();
		topicCategory.setId(SequenceUtils.nexSeqId());
		topicCategoryMapper.insertSelective(topicCategory);
	}

	@Override
	public void updateTopicCategory(TopicCategoryRequest request, String id) {

		TopicCategory topicCategory = request.buildTopicCategory();
		topicCategory.setId(id);
		topicCategoryMapper.updateByPrimaryKey(topicCategory);
	}

	@Override
	public void deleteTopicCategory(String id, String language) {

		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("language", language);
		topicCategoryMapper.deleteTopicCategoryById(params);
	}

	@Override
	public TopicCategory getTopicCategoryById(String id, String language) {

		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("language", language);
		return topicCategoryMapper.findOne(params);
	}

	@Override
	public TopicCategory getCategoryByCode(String code, String language) {

		Map<String, Object> params = new HashMap<>();
		params.put("code", code);
		params.put("language", language);
		return topicCategoryMapper.findOneByCode(params);
	}

	@Override
	public List<TopicCategory> getCategoriesByParentId(String parentId, String language) {

		Map<String, Object> params = new HashMap<>();
		params.put("pid", parentId);
		params.put("language", language);
		return topicCategoryMapper.findCategoriesByParentId(params);
	}


}
