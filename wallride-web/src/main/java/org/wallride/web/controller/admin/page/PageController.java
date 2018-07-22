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

package org.wallride.web.controller.admin.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.wallride.support.AuthorizedUser;
import org.wallride.web.support.Pagination;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.groups.Default;

@Controller
@RequestMapping("/{language}/pages")
public class PageController {
	
	@Autowired
	private PageService pageService;

	@ModelAttribute("countAll")
	public long countAll(@PathVariable String language) {
		return pageService.countPages(language);
	}

	@ModelAttribute("countDraft")
	public long countDraft(@PathVariable String language) {
		return pageService.countPagesByStatus(Post.Status.DRAFT, language);
	}

	@ModelAttribute("countScheduled")
	public long countScheduled(@PathVariable String language) {
		return pageService.countPagesByStatus(Post.Status.SCHEDULED, language);
	}

	@ModelAttribute("countPublished")
	public long countPublished(@PathVariable String language) {
		return pageService.countPagesByStatus(Post.Status.PUBLISHED, language);
	}

	@ModelAttribute("form")
	public PageSearchForm setupPageSearchForm() {
		return new PageSearchForm();
	}

	@ModelAttribute("query")
	public String query(@RequestParam(required = false) String query) {
		return query;
	}

	@GetMapping(value = "/index")
	public String index() {
		return "page/index";
	}

	@GetMapping
	public String search(
			@PathVariable String language,
			@Validated @ModelAttribute("form") PageSearchForm form,
			BindingResult result,
			@PageableDefault Pageable pageable,
			Model model,
			HttpServletRequest servletRequest) {
		org.springframework.data.domain.Page<Page> pages = pageService.getPages(form.toPageSearchRequest(), pageable);

		model.addAttribute("pages", pages);
		model.addAttribute("pageable", pageable);
		model.addAttribute("pagination", new Pagination<>(pages, servletRequest));
		return "page/pages";
	}

	@PostMapping
	public ResponseEntity savePage(@PathVariable String language, @Validated({Default.class, PageCreateForm.GroupPublish.class}) @ModelAttribute("form") PageCreateForm form,
			AuthorizedUser authorizedUser) {

		pageService.createPage(form.buildPageCreateRequest(), Post.Status.PUBLISHED, authorizedUser);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping
	public ResponseEntity bulkDelete(@Valid @ModelAttribute("form") PageBulkDeleteForm form) {

		pageService.bulkDeletePage(form.buildPageBulkDeleteRequest());
		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

	@RequestMapping("/{id}")
	public String describe(@PathVariable String language, @PathVariable Long id, Model model) {

		Page page = pageService.getPageById(id);
		if (!page.getLanguage().equals(language)) {
			pageService.getPageByCode(page.getCode(), language);
		}
		model.addAttribute("page", page);
		return "page/describe";
	}

}