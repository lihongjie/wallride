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

package org.wallride.web.controller.guest.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.domain.*;
import org.wallride.model.ArticleArchiveResponse;
import org.wallride.model.CommentCreateRequest;
import org.wallride.service.ArticleService;
import org.wallride.service.TopicCategoryService;
import org.wallride.service.CommentService;
import org.wallride.support.AuthorizedUser;
import org.wallride.web.support.HttpNotFoundException;
import org.wallride.web.support.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping
public class ArticleDescribeController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private TopicCategoryService topicCategoryService;

	@GetMapping("/{year:[0-9]{4}}/{month:[0-9]{2}}/{day:[0-9]{2}}/{code:.+}")
	public String describe(@PathVariable String code, Language language, Model model, AuthorizedUser authorizedUser) {
		Article article = articleService.getArticleByCode(code, language.getLanguage());
		if (article == null) {
			article = articleService.getArticleByCode(code, language.getBlog().getDefaultLanguage());
		}
		if (article == null) {
			throw new HttpNotFoundException();
		}
		if (article.getStatus() != Post.Status.PUBLISHED) {
			throw new HttpNotFoundException();
		}

//		CommentSearchRequest request = new CommentSearchRequest();
//		request.setPostId(article.getId());
//		request.setApproved(Boolean.TRUE);
		Long articleId = article.getId();
		Article prevArticle = articleService.getPrevArticle(articleId);
		Article nextArticle = articleService.getNextArticle(articleId);

		model.addAttribute("prev", prevArticle);
		model.addAttribute("next", nextArticle);
		model.addAttribute("article", article);
		model.addAttribute("authorizedUser", authorizedUser);
		return "article/describe";
	}

	@PostMapping("/article/{id}/comments")
	public ResponseEntity postComment(CommentCreateRequest commentCreateRequest) {
		User user = new User();
		user.setId(13L);
		commentCreateRequest.setAuthorId(13L);
		AuthorizedUser authorizedUser = new AuthorizedUser(user);
		commentService.createComment(commentCreateRequest, authorizedUser);
		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

	@GetMapping("/article/{id}/comments")
	public String comments(
			@PathVariable Long id,
			@PageableDefault Pageable pageable,
			HttpServletRequest servletRequest,
			Model model) {

		Page<Comment> comments = commentService.getCommentsByArticleId(id, pageable);
		long totalCount = commentService.totalCountCommentForArticle(id);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("comments", comments);
		model.addAttribute("pageable", pageable);
		model.addAttribute("pagination", new Pagination<>(comments, servletRequest));
		return "article/comments";
	}

	@GetMapping("/article/{id}/categories")
	public String categoryArchive(Model model) {

		List<CategoryResponse> categories = topicCategoryService.categoryGroup();
		model.addAttribute("cagegories", categories);
		return "article/category-archive";
	}

	@GetMapping("/article/{id}/archive")
	public String articleArchive(@PathVariable Long id, Model model) {

		List<ArticleArchiveResponse> articleArchives = articleService.articleArchive(id);
		model.addAttribute("articleArchives", articleArchives);
		return "article/article-archive";
	}

	@GetMapping("/article/{id}/tags")
	public String tags(@PathVariable Long id, Model model) {

		List<Tag> tags = articleService.tagsArchive(id);
		model.addAttribute("tags", tags);
		return "article/tags";
	}
}
