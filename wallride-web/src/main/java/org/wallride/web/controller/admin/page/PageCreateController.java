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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.wallride.model.TreeNode;
import org.wallride.web.support.RestValidationErrorModel;

import javax.inject.Inject;
import java.util.List;
import java.util.SortedSet;

@Controller
@RequestMapping("/{language}/pages/create")
public class PageCreateController {

	private static Logger logger = LoggerFactory.getLogger(PageCreateController.class);

	@Inject
	private PageService pageService;

	@Inject
	private CategoryUtils categoryUtils;

	@Inject
	private CustomFieldService customFieldService;

	@Inject
	private MessageSourceAccessor messageSourceAccessor;

	@ModelAttribute("form")
	public PageCreateForm pageCreateForm(
			@PathVariable String language,
			@RequestParam(required = false) String code) {
		SortedSet<CustomField> customFields = customFieldService.getAllCustomFields(language);
		PageCreateForm form = new PageCreateForm(customFields);
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

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody RestValidationErrorModel bindException(BindException e) {
		logger.debug("BindException", e);
		return RestValidationErrorModel.fromBindingResult(e.getBindingResult(), messageSourceAccessor);
	}

	@RequestMapping(method=RequestMethod.GET)
	public String create() {
		return "page/create";
	}

	@RequestMapping(method=RequestMethod.GET, params="part=page-fieldset")
	public String partPageFieldset(@PathVariable String language, Model model) {
		return "page/create::#page-fieldset";
	}

	@RequestMapping(method=RequestMethod.GET, params="part=category-fieldset")
	public String partCategoryFieldset(@PathVariable String language) {
		return "page/create::#category-fieldset";
	}




}