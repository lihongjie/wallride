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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.wallride.domain.Article;
import org.wallride.domain.Category;
import org.wallride.domain.Post;
import org.wallride.model.*;
import org.wallride.service.ArticleService;
import org.wallride.service.CategoryService;
import org.wallride.support.AuthorizedUser;
import org.wallride.support.CategoryUtils;
import org.wallride.web.support.Pagination;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/{language}/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryUtils categoryUtils;

    @ModelAttribute("countAll")
    public long countAll(@PathVariable String language) {
        return articleService.countArticles(language);
    }

    @ModelAttribute("countDraft")
    public long countDraft(@PathVariable String language) {
        return articleService.countArticlesByStatus(Post.Status.DRAFT, language);
    }

    @ModelAttribute("countScheduled")
    public long countScheduled(@PathVariable String language) {
        return articleService.countArticlesByStatus(Post.Status.SCHEDULED, language);
    }

    @ModelAttribute("countPublished")
    public long countPublished(@PathVariable String language) {
        return articleService.countArticlesByStatus(Post.Status.PUBLISHED, language);
    }

    @GetMapping(value = "/index")
    @Transactional
    public String index(AuthorizedUser authorizedUser, Model model) {

        model.addAttribute("author", authorizedUser);
        return "article/index";
    }

    @GetMapping
    public String search(
            @PathVariable String language,
            ArticleSearchForm form,
            @PageableDefault Pageable pageable,
            Model model,
            HttpServletRequest servletRequest) {

        Page<Article> articles = articleService.getArticles(form.toArticleSearchRequest(), pageable);

        model.addAttribute("articles", articles);
        model.addAttribute("pageable", pageable);
        model.addAttribute("pagination", new Pagination<>(articles, servletRequest));
        return "article/articles";
    }

    @GetMapping(value = "/create")
    public String create(AuthorizedUser authorizedUser, Model model) {

        List<TreeNode<Category>> categories = categoryUtils.getNodes(true);
        List<Category> categoryList = categoryService.getCategories(authorizedUser);
        model.addAttribute("author", authorizedUser);
        model.addAttribute("categoryNodes", categories);
        return "article/create";
    }

    @PostMapping(params="status=draft")
    public ResponseEntity saveAsDraft(
            @PathVariable String language,
            @Valid ArticleRequest articleRequest,
            AuthorizedUser authorizedUser) {

        articleService.saveArticleAsDraft(articleRequest, authorizedUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping(params="status=publish")
    public ResponseEntity saveAsPublished(
            @PathVariable String language,
            @Valid ArticleRequest articleRequest,
            AuthorizedUser authorizedUser) {

        articleService.saveArticleAsPublished(articleRequest, authorizedUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping(value = "/{id}")
    public String edit(
            @PathVariable String language,
            @RequestParam Long id,
            Model model) {

        Article article = articleService.getArticleById(id, language);
        model.addAttribute("article", article);

        return "article/edit";
    }

    @PutMapping(value = "/{id}", params="draft")
    public ResponseEntity updateAsDraft(
            @PathVariable String language,
            @PathVariable Long id,
            ArticleRequest articleRequest,
            AuthorizedUser authorizedUser) {

        articleService.updateArticleAsDraft(articleRequest, authorizedUser, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PutMapping(value = "/{id}", params="publish")
    public ResponseEntity updateAsPublished(
            @PathVariable String language,
            @PathVariable Long id,
            ArticleRequest articleRequest,
            AuthorizedUser authorizedUser) throws ParseException {

        articleService.updateArticleAsPublished(articleRequest, authorizedUser, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping(value = "/bulk-publish")
    public ResponseEntity bulkPublish(
            @Valid ArticleBulkPublishRequest articleRequest,
            BindingResult bindingResult,
            AuthorizedUser authorizedUser) {

        articleService.bulkPublishArticle(articleRequest, authorizedUser);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping(value = "/bulk-unpublish")
    public ResponseEntity unpublish(
            @Valid ArticleBulkUnpublishRequest articleRequest, BindingResult bindingResult,
            AuthorizedUser authorizedUser) {

        articleService.bulkUnpublishArticle(articleRequest, authorizedUser);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping
    public ResponseEntity delete(ArticleBulkDeleteRequest articleRequest) {

        articleService.deleteArticle(articleRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

}
