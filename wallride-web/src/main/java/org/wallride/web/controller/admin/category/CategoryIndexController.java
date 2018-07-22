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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wallride.service.ArticleService;
import org.wallride.service.TopicCategoryService;

import java.util.Map;

@Controller
@RequestMapping("/{language}/categories/nodes")
public class CategoryIndexController {

	@Autowired
	private TopicCategoryService topicCategoryService;

	@Autowired
	private CategoryUtils categoryUtils;

	@Autowired
	private ArticleService articleService;

	@ModelAttribute("form")
	public CategoryCreateForm categoryCreateForm() {
		return new CategoryCreateForm();
	}

	@ModelAttribute("articleCounts")
	public Map<Long, Long> articleCounts(@PathVariable String language) {
		return articleService.countArticlesByCategoryIdGrouped(Post.Status.PUBLISHED, language);
	}

	@RequestMapping
	public String index(@PathVariable String language, Model model) {
		model.addAttribute("categoryNodes", categoryUtils.getNodes(true));
		return "category/index";
	}
}