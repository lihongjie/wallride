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

package org.wallride.web.support;

import org.springframework.util.ObjectUtils;
import org.springframework.web.util.UrlPathHelper;
import org.tuckey.web.filters.urlrewrite.extend.RewriteMatch;
import org.tuckey.web.filters.urlrewrite.extend.RewriteRule;
import org.wallride.autoconfigure.WallRideServletConfiguration;
import org.wallride.domain.Language;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BlogLanguageRewriteRule extends RewriteRule {

	private BlogService blogService;

	public BlogLanguageRewriteRule(BlogService blogService) {
		this.blogService = blogService;
	}

	// https://github.com/paultuckey/urlrewritefilter/issues/136
//	@Override
//	public boolean initialise(ServletContext servletContext) {
//		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
//		blogService = context.getBean(BlogService.class);
//		return super.initialise(servletContext);
//	}

	@Override
	public RewriteMatch matches(HttpServletRequest request, HttpServletResponse response) {
		UrlPathHelper urlPathHelper = new UrlPathHelper();

		String servletPath = urlPathHelper.getOriginatingServletPath(request);
		if (ObjectUtils.nullSafeEquals(servletPath, WallRideServletConfiguration.ADMIN_SERVLET_PATH)) {
			return null;
		}

		String lookupPath = urlPathHelper.getLookupPathForRequest(request);

		Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
		if (blog == null) {
			return null;
		}

		Language matchedLanguage = null;
		for (Language language : blog.getLanguages()) {
			if (lookupPath.startsWith("/" + language.getLanguage() + "/")) {
				matchedLanguage = language;
				break;
			}
		}

		if (matchedLanguage == null) {
			matchedLanguage = blog.getLanguage(blog.getDefaultLanguage());
		}

		return new BlogLanguageRewriteMatch(matchedLanguage);
	}
}
