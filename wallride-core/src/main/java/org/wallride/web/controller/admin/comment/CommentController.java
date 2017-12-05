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

package org.wallride.web.controller.admin.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.wallride.domain.Comment;
import org.wallride.exception.ServiceException;
import org.wallride.service.CommentService;
import org.wallride.support.AuthorizedUser;
import org.wallride.web.support.ControllerUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping("/{language}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping(value = "/index")
    public String index() {
        return "comment/index";
    }

    @GetMapping
    public String search(
            @PathVariable String language,
            CommentSearchForm form,
            @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            HttpServletRequest servletRequest) {

        Page<Comment> comments = commentService.getComments(form.toCommentSearchRequest(), pageable);
        model.addAttribute("comments", comments);
        model.addAttribute("pageable", pageable);
        model.addAttribute("pagination", new org.wallride.web.support.Pagination<>(comments, servletRequest));
        return "comment/comments";
    }

    @PutMapping(value = "/bulk-unapprove")
    public ResponseEntity unapprove(CommentBulkUnapproveForm form, AuthorizedUser authorizedUser) {

        commentService.bulkUnapproveComment(form.toCommentBulkUnapproveRequest(), authorizedUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PutMapping(value = "/bulk-approve")
    public ResponseEntity approve(CommentBulkApproveForm form, AuthorizedUser authorizedUser) {

       commentService.bulkApproveComment(form.toCommentBulkApproveRequest(), authorizedUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


    @DeleteMapping
    public ResponseEntity delete(CommentBulkDeleteForm form) {

        commentService.bulkDeleteComment(form.toCommentBulkDeleteRequest());
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
