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

package org.wallride.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.domain.Article;
import org.wallride.domain.Comment;
import org.wallride.domain.Post;
import org.wallride.domain.User;
import org.wallride.exception.ServiceException;
import org.wallride.model.*;
import org.wallride.repository.ArticleRepository;
import org.wallride.repository.CommentRepository;
import org.wallride.repository.PostRepository;
import org.wallride.repository.UserRepository;
import org.wallride.service.CommentService;
import org.wallride.support.AuthorizedUser;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private static Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Comment createComment(CommentCreateRequest request, AuthorizedUser authorizedUser) {
        Post post = postRepository.findOneByIdAndLanguage(request.getPostId(), request.getBlogLanguage().getLanguage());
        if (post == null) {
            throw new ServiceException("Post was not found [" + request.getPostId() + "]");
        }
        User author = userRepository.findOneById(request.getAuthorId());
        Comment comment = new Comment();
        String userName = authorizedUser.toString();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setAuthorName(author.toString());
        comment.setDate(request.getDate());
        comment.setContent(request.getContent());
        comment.setApproved(false);
        comment.setCreatedBy(userName);
        comment.setUpdatedBy(userName);
        return commentRepository.save(comment);
    }

    @Override
    public void bulkApproveComment(CommentBulkApproveRequest request, AuthorizedUser authorizedUser) {
        updateCommentStatus(request.getIds(), true, authorizedUser.toString());
    }

    @Override
    public void bulkUnapproveComment(CommentBulkUnapproveRequest request, AuthorizedUser authorizedUser) {

        updateCommentStatus(request.getIds(), false, authorizedUser.toString());
    }

    @Override
    public void deleteComment(CommentDeleteRequest deleteRequest) {
        commentRepository.delete(deleteRequest.getId());
    }

    @Transactional
    @Override
    public void bulkDeleteComment(CommentBulkDeleteRequest bulkDeleteRequest) {

        List<Comment> comments = commentRepository.findAll(bulkDeleteRequest.getIds());
        commentRepository.deleteInBatch(comments);
    }

    @Override
    public Page<Comment> getComments(CommentSearchRequest request, Pageable pageable) {
//		return commentRepository.search(request, pageable);
        return commentRepository.findAll(pageable);
    }

    @Override
    public Page<Comment> getCommentsByArticleId(Long articleId, Pageable pageable) {
        Article article = articleRepository.findOne(articleId);
        return commentRepository.findAllByPost(article, pageable);
    }

    @Override
    public long totalCountCommentForArticle(Long articleId) {

        return commentRepository.countCommentByPostId(articleId);
    }

    private void updateCommentStatus(List<Long> ids, Boolean status, String updatedBy) {

        List<Comment> comments = commentRepository.findAll(ids);
        LocalDateTime now = LocalDateTime.now();
        for (Comment comment : comments) {
            comment.setApproved(status);
            comment.setUpdatedAt(now);
            comment.setUpdatedBy(updatedBy);
        }
        commentRepository.save(comments);
    }
}
