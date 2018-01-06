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

package org.wallride.web.controller.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.wallride.domain.Post;
import org.wallride.domain.User;
import org.wallride.model.UserBulkDeleteRequest;
import org.wallride.service.ArticleService;
import org.wallride.service.UserService;
import org.wallride.support.AuthorizedUser;
import org.wallride.web.support.Pagination;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/{language}/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ArticleService articleService;

	@ModelAttribute("articleCounts")
	public Map<Long, Long> articleCounts(@PathVariable String language) {
		return articleService.countArticlesByAuthorIdGrouped(Post.Status.PUBLISHED, language);
	}

//	@ModelAttribute("form")
//	public UserSearchForm setupUserSearchForm() {
//		return new UserSearchForm();
//	}
//
//	@ModelAttribute("query")
//	public String query(@RequestParam(required = false) String query) {
//		return query;
//	}

	@GetMapping(value = "/index")
	public String index(AuthorizedUser authorizedUser, Model model) {

		model.addAttribute("author", authorizedUser);
		return "user/index";
	}

	@GetMapping
	public String search(
			@PathVariable String language,
			UserSearchForm form,
			BindingResult result,
			@PageableDefault Pageable pageable,
			Model model,
			HttpServletRequest servletRequest) {

		Page<User> users = userService.getUsers(form.toUserSearchRequest(), pageable);
		model.addAttribute("users", users);
		model.addAttribute("pageable", pageable);
		model.addAttribute("pagination", new Pagination<>(users, servletRequest));
		return "user/users";
	}

	@DeleteMapping
	public ResponseEntity delete(
			UserBulkDeleteRequest deleteRequest,
			BindingResult bindingResult) {

		userService.bulkDeleteUser(deleteRequest);

		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

	@GetMapping(value = "/{id}")
	public String edit(@PathVariable Long id, Model model) {
		User user = userService.getUserById(id);
		UserEditForm form = UserEditForm.fromDomainObject(user);
		model.addAttribute("form", form);
		return "user/edit";
	}

	@PutMapping
	public ResponseEntity updateUser(
			UserEditForm form,
			BindingResult errors,
			AuthorizedUser authorizedUser) {

		User user = userService.updateUser(form.buildUserUpdateRequest(), authorizedUser);

		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}


	@GetMapping(value = "/describe")
	public String describe(
			@PathVariable String language,
			@RequestParam long id, Model model) {
		User user = userService.getUserById(id);

		model.addAttribute("user", user);
		return "user/describe";
	}

}