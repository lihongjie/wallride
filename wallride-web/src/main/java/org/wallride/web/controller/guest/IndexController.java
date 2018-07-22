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

package org.wallride.web.controller.guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.domain.Article;
import org.wallride.domain.Language;
import org.wallride.domain.PopularPost;
import org.wallride.service.ArticleService;
import org.wallride.service.PostService;
import org.wallride.support.AuthorizedUser;
import org.wallride.web.controller.guest.article.ArticleSearchForm;
import org.wallride.web.support.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Controller
@RequestMapping("/")
public class IndexController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private PostService postService;

	@GetMapping
	public String index() {

		return "index";
	}

	@GetMapping(value = "/articles")
//	@Transactional
	public String home(
			@PageableDefault(5) Pageable pageable,
			Language language,
			Model model,
			HttpServletRequest servletRequest, AuthorizedUser authorizedUser) {

		ArticleSearchForm form = new ArticleSearchForm();
		form.setLanguage(language.getLanguage());
		Page<Article> articles = articleService.getArticles(form.toArticleSearchRequest(), pageable);
		model.addAttribute("articles", articles);
		model.addAttribute("pageable", pageable);
		model.addAttribute("pagination", new Pagination<>(articles, servletRequest));
		model.addAttribute("authorizedUser", authorizedUser);
		return "articles";
	}

	@GetMapping(value = "/popular_post")
	public String popularPosts(Model model) {

		Set<PopularPost> popularPostList = postService.getPopularPosts("en", PopularPost.Type.WEEKLY);
		model.addAttribute("popularPosts", popularPostList);
		return "articles/popular-post";

	}
}
