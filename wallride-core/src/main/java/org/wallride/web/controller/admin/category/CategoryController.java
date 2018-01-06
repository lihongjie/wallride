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

package org.wallride.web.controller.admin.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.wallride.domain.Category;
import org.wallride.service.CategoryService;
import org.wallride.support.AuthorizedUser;
import org.wallride.support.CategoryUtils;
import org.wallride.web.support.Pagination;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(value="/{language}/categories")
public class CategoryController {

	private static Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CategoryUtils categoryUtils;


	@GetMapping(value = "/index")
	public String index(AuthorizedUser authorizedUser, Model model) {

		model.addAttribute("author", authorizedUser);
		return "category/index";
	}

	@GetMapping
	public String search(@PathVariable String language, CategorySearchForm form,
						 @PageableDefault Pageable pageable,
						 Model model,
						 HttpServletRequest servletRequest) {

		Page<Category> categories = categoryService.getCategories(form.toCategorySearchRequest(), pageable);
		model.addAttribute("categories", categories);
		model.addAttribute("pageable", pageable);
		model.addAttribute("pagination", new Pagination<>(categories, servletRequest));
		return "category/categories";
	}

	@PutMapping
	public ResponseEntity save(AuthorizedUser authorizedUser, @Valid CategoryCreateForm form) {

		categoryService.createCategory(form.buildCategoryCreateRequest(), authorizedUser);
		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

	@PutMapping(value="/{id}")
	public ResponseEntity update(
			@Valid CategoryEditForm form,
			@PathVariable Long id,
			AuthorizedUser authorizedUser) {

		form.setId(id);
		categoryService.updateCategory(form.buildCategoryUpdateRequest(), authorizedUser);
		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

	@DeleteMapping(value="/{id}")
	public ResponseEntity delete(
			@PathVariable String language,
			@PathVariable Long id) {

		categoryService.deleteCategory(id, language);
		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

//	@RequestMapping(value="/{language}/categories", method= RequestMethod.PUT, consumes=MediaType.APPLICATION_JSON_VALUE)
//	public @ResponseBody CategoryIndexModel sort(@PathVariable String language, @RequestBody List<Map<String, Object>> data) {
//		categoryService.updateCategoryHierarchy(data, language);
//		return new CategoryIndexModel(categoryUtils.getNodes(true));
//	}
}
