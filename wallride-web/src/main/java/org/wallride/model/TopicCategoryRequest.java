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

package org.wallride.model;

import org.wallride.domain.TopicCategory;

import java.io.Serializable;

public class TopicCategoryRequest implements Serializable {

	private String parentId;

	private String name;

	private String language;

	private String description;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public TopicCategory buildTopicCategory() {
		TopicCategory topicCategory = new TopicCategory();
		topicCategory.setParentId(this.parentId);
		topicCategory.setLanguage(this.language);
		topicCategory.setName(this.name);
		topicCategory.setDescription(this.description);
		return topicCategory;
	}
}
