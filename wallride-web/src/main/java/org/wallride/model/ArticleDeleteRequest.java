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

import java.io.Serializable;
import java.util.List;

public class ArticleDeleteRequest implements Serializable {

	private List<String> ids;

	private String language;

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public static class Builder  {

		private List<String> ids;
		private String language;

		public Builder() {
		}

		public Builder ids(List<String> ids) {
			this.ids = ids;
			return this;
		}

		public Builder language(String language) {
			this.language = language;
			return this;
		}

		public ArticleDeleteRequest build() {
			ArticleDeleteRequest request = new ArticleDeleteRequest();
			request.ids = ids;
			request.language = language;
			return request;
		}
	}
}
