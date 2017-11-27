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

package org.wallride.web.controller.admin.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.wallride.domain.Article;
import org.wallride.domain.Category;
import org.wallride.domain.CustomField;
import org.wallride.domain.Post;
import org.wallride.model.TreeNode;
import org.wallride.service.ArticleService;
import org.wallride.service.CustomFieldService;
import org.wallride.support.AuthorizedUser;
import org.wallride.support.CategoryUtils;
import org.wallride.web.support.DomainObjectSavedModel;

import javax.validation.groups.Default;
import java.text.ParseException;
import java.util.List;
import java.util.SortedSet;

@Controller
@RequestMapping("/{language}/articles/create")
public class ArticleCreateController {

	private static Logger logger = LoggerFactory.getLogger(ArticleCreateController.class);

	@Autowired
	private ArticleService articleService;

	@Autowired
	private CategoryUtils categoryUtils;

	@Autowired
	private CustomFieldService customFieldService;

	@ModelAttribute("form")
	public ArticleCreateForm articleCreateForm(
			@PathVariable String language,
			@RequestParam(required = false) String code) {
		SortedSet<CustomField> customFields = customFieldService.getAllCustomFields(language);
		ArticleCreateForm form = new ArticleCreateForm(customFields);
		if (code != null) {
			form.setCode(code);
		}
		return form;
	}

	@ModelAttribute("categoryNodes")
	public List<TreeNode<Category>> setupCategoryNodes(@PathVariable String language) {
		return categoryUtils.getNodes(true);
	}

	@ModelAttribute("query")
	public String query(@RequestParam(required = false) String query) {
		return query;
	}

	@RequestMapping(method=RequestMethod.GET)
	public String create() {

		return "article/create";
	}

	@RequestMapping(value = "/metronic", method=RequestMethod.GET)
	public String create_metronic() {

		return "article/create_metronic";
	}

	@RequestMapping(method=RequestMethod.GET, params="part=category-fieldset")
	public String partCategoryFieldset(@PathVariable String language) {
		return "article/create::#category-fieldset";
	}

	@RequestMapping(method=RequestMethod.POST, params="draft")
	public @ResponseBody DomainObjectSavedModel saveAsDraft(
			@PathVariable String language,
			@Validated @ModelAttribute("form") ArticleCreateForm form,
			AuthorizedUser authorizedUser) throws ParseException {

		Article article = articleService.createArticle(form.buildArticleCreateRequest(), Post.Status.DRAFT, authorizedUser);
		return new DomainObjectSavedModel<>(article);
	}

	@RequestMapping(method=RequestMethod.POST, params="publish")
	public String saveAsPublished(
			@PathVariable String language,
			@Validated({Default.class, ArticleCreateForm.GroupPublish.class}) @ModelAttribute("form") ArticleCreateForm form,
			AuthorizedUser authorizedUser) throws ParseException {

		Article article = articleService.createArticle(form.buildArticleCreateRequest(), Post.Status.PUBLISHED, authorizedUser);

		return "redirect:/_admin/{language}/articles/index";
	}

}