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

package org.wallride.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wallride.domain.Comment;
import org.wallride.model.*;
import org.wallride.support.AuthorizedUser;

public interface CommentService {

    Comment createComment(CommentCreateRequest request, AuthorizedUser authorizedUser);

    void bulkApproveComment(CommentBulkApproveRequest request, AuthorizedUser authorizedUser);

    void bulkUnapproveComment(CommentBulkUnapproveRequest request, AuthorizedUser authorizedUser);

    void deleteComment(CommentDeleteRequest deleteRequest);

    void bulkDeleteComment(CommentBulkDeleteRequest bulkDeleteRequest);

    Page<Comment> getComments(CommentSearchRequest request, Pageable pageable);

    Page<Comment> getCommentsByArticleId(Long articleId, Pageable pageable);

    long totalCountCommentForArticle(Long articleId);
}
