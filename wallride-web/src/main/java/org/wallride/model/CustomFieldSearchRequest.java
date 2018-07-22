package org.wallride.model;


import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.wallride.domain.Language;

import java.io.Serializable;

public class CustomFieldSearchRequest implements Serializable {

	private String keyword;
	private String language;

	public CustomFieldSearchRequest() {
		this.language = LocaleContextHolder.getLocale().getLanguage();
	}

	public CustomFieldSearchRequest(Language language) {
		this.language = language.getLanguage();
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public CustomFieldSearchRequest withKeyword(String keyword) {
		this.keyword = keyword;
		return this;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public CustomFieldSearchRequest withLanguage(String language) {
		this.language = language;
		return this;
	}

	public boolean isEmpty() {
		if (StringUtils.hasText(getKeyword())) {
			return false;
		}
		if (StringUtils.hasText(getLanguage())) {
			return false;
		}
		return true;
	}
}
