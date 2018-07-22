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

package org.wallride.web.controller.admin.tag;

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
import org.wallride.domain.Tag;
import org.wallride.service.ArticleService;
import org.wallride.service.TagService;
import org.wallride.support.AuthorizedUser;
import org.wallride.web.support.Pagination;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/{language}/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleService articleService;


//    @ModelAttribute("articleCounts")
//    public Map<Long, Long> articleCounts(@PathVariable String language) {
//        return articleService.countArticlesByTagIdGrouped(Post.Status.PUBLISHED, language);
//    }

    @GetMapping(value = "/index")
    public String index(AuthorizedUser authorizedUser, Model model) {

        model.addAttribute("author", authorizedUser);
        return "tag/index";
    }

    @GetMapping
    public String search(
            @PathVariable String language, TagSearchForm form, @PageableDefault Pageable pageable, Model model,
            HttpServletRequest servletRequest) {
        Page<Tag> tags = tagService.getTags(form.toTagSearchRequest(), pageable);

        model.addAttribute("tags", tags);
        model.addAttribute("pageable", pageable);
        model.addAttribute("pagination", new Pagination<>(tags, servletRequest));
        return "tag/tags";
    }

    @PostMapping
    public ResponseEntity save(@Valid TagCreateForm form, AuthorizedUser authorizedUser) {

        tagService.createTag(form.buildTagCreateRequest(), authorizedUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity update(@Valid TagEditForm form, @PathVariable Long id, AuthorizedUser authorizedUser) {

        form.setId(id);
        tagService.updateTag(form.buildTagUpdateRequest(), authorizedUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping(value = "/merge")
    public ResponseEntity merge(@Valid TagMergeForm form, AuthorizedUser authorizedUser) {

        tagService.mergeTags(form.toTagMergeRequest(), authorizedUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping
    public ResponseEntity delete(@Valid TagBulkDeleteForm form, BindingResult errors) {

        tagService.bulkDeleteTag(form.buildTagBulkDeleteRequest());
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}