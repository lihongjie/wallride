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

import org.springframework.format.annotation.DateTimeFormat;
import org.wallride.web.controller.admin.article.CustomFieldValueEditForm;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ArticleRequest model for creating and updating article
 */
public class ArticleRequest implements Serializable {

	private String language;

//	@NotBlank
	private String code;

//	@NotBlank
	private String coverId;

//	@NotBlank
	private String title;

//	@NotBlank
	private String body;

	private Long authorId;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime date;

	private Set<Long> categoryIds = new HashSet<>();

	private Set<String> tags = new HashSet<>();

	private Set<Long> relatedPostIds = new HashSet<>();

	private String seoTitle;

	private String seoDescription;

	private String seoKeywords;

	private List<CustomFieldValueEditForm> customFieldValues = new ArrayList<>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCoverId() {
		return coverId;
	}

	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Set<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(Set<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public Set<Long> getRelatedPostIds() {
		return relatedPostIds;
	}

	public void setRelatedPostIds(Set<Long> relatedPostIds) {
		this.relatedPostIds = relatedPostIds;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public List<CustomFieldValueEditForm> getCustomFieldValues() {
		return customFieldValues;
	}

	public void setCustomFieldValues(List<CustomFieldValueEditForm> customFieldValues) {
		this.customFieldValues = customFieldValues;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
