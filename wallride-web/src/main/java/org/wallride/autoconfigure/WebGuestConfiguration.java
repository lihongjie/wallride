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

package org.wallride.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.wallride.web.controller.guest.*;
import org.wallride.web.controller.guest.article.ArticleDescribeController;
import org.wallride.web.controller.guest.article.ArticleController;
import org.wallride.web.controller.guest.comment.CommentController;
import org.wallride.web.controller.guest.page.PageDescribeController;
import org.wallride.web.controller.guest.user.*;
import org.wallride.web.support.AuthorizedUserMethodArgumentResolver;

import java.util.List;

@Configuration
public class WebGuestConfiguration extends DelegatingWebMvcConfiguration {

	@Autowired
	private PageDescribeController pageDescribeController;

	@Override
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
		RequestMappingHandlerMapping handlerMapping = super.requestMappingHandlerMapping();
		handlerMapping.setOrder(Ordered.LOWEST_PRECEDENCE);
		return handlerMapping;
	}

	@Override
	protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
		RequestMappingHandlerMapping handlerMapping = super.createRequestMappingHandlerMapping();
		handlerMapping.setDefaultHandler(pageDescribeController);
		return handlerMapping;
	}

	@Override
	protected RequestMappingHandlerAdapter createRequestMappingHandlerAdapter() {

		return super.createRequestMappingHandlerAdapter();
	}

	@Override
	protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new AuthorizedUserMethodArgumentResolver());
		super.addArgumentResolvers(argumentResolvers);
	}

	@Configuration
	public static class ControllerConfigration {

		@Autowired
		private BlogService blogService;

		@Autowired
		private PageService pageService;

		@Bean
		@ConditionalOnMissingBean
		public PageDescribeController pageDescribeController() {
			return new PageDescribeController(blogService, pageService);
		}

		@Bean
		@ConditionalOnMissingBean
		public ArticleDescribeController articleDescribeController() {
			return new ArticleDescribeController();
		}

		@Bean
		@ConditionalOnMissingBean
		public ArticleController articleIndexController() {
			return new ArticleController();
		}

		@Bean
		@ConditionalOnMissingBean
		public CommentController commentRestController() {
			return new CommentController();
		}

		@Bean
		@ConditionalOnMissingBean
		public LoginController loginController() {
			return new LoginController();
		}

		@Bean
		@ConditionalOnMissingBean
		public PasswordResetController passwordResetController() {
			return new PasswordResetController();
		}

		@Bean
		@ConditionalOnMissingBean
		public PasswordUpdateController passwordUpdateController() {
			return new PasswordUpdateController();
		}

		@Bean
		@ConditionalOnMissingBean
		public ProfileUpdateController profileUpdateController() {
			return new ProfileUpdateController();
		}

		@Bean
		@ConditionalOnMissingBean
		public SignupController signupController() {
			return new SignupController();
		}

		@Bean
		@ConditionalOnMissingBean
		public FeedController feedController() {
			return new FeedController();
		}

		@Bean
		@ConditionalOnMissingBean
		public IndexController indexController() {
			return new IndexController();
		}

		@Bean
		@ConditionalOnMissingBean
		public SearchController searchController() {
			return new SearchController();
		}

		@Bean
		@ConditionalOnMissingBean
		public CategoryController categoryController() {
			return new CategoryController();
		}
		
		@Bean
		@ConditionalOnMissingBean
		public TagController tagController() {
			return new TagController();
		}
	}
}
