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
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.wallride.domain.PasswordResetToken;
import org.wallride.domain.User;
import org.wallride.domain.UserInvitation;
import org.wallride.exception.DuplicateEmailException;
import org.wallride.exception.DuplicateLoginIdException;
import org.wallride.exception.EmailNotFoundException;
import org.wallride.exception.ServiceException;
import org.wallride.mapper.PasswordResetTokenMapper;
import org.wallride.mapper.UserInvitationMapper;
import org.wallride.mapper.UserMapper;
import org.wallride.model.*;
import org.wallride.service.UserService;
import org.wallride.support.AuthorizedUser;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private BlogService blogService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInvitationMapper userInvitationMapper;

    @Autowired
    private PasswordResetTokenMapper passwordResetTokenMapper;

    @Override
    public PasswordResetToken createPasswordResetToken(PasswordResetTokenCreateRequest request) {

        User user = userMapper.findOneByEmail(request.getEmail());
        if (user == null) {
            throw new EmailNotFoundException();
        }

        PasswordResetToken passwordResetToken = user.buildPasswordResetToken();

        passwordResetTokenMapper.insertSelective(passwordResetToken);

        try {
            Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
            String blogTitle = blog.getTitle(LocaleContextHolder.getLocale().getLanguage());

            ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
            if (blog.isMultiLanguage()) {
                builder.path("/{language}");
            }
            builder.path("/password-reset");
            builder.path("/{token}");

            Map<String, Object> urlVariables = new LinkedHashMap<>();
            urlVariables.put("language", request.getLanguage());
            urlVariables.put("token", passwordResetToken.getToken());
            String resetLink = builder.buildAndExpand(urlVariables).toString();

            Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("passwordResetToken", passwordResetToken);
            ctx.setVariable("resetLink", resetLink);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
            message.setFrom(mailProperties.getProperties().get("mail.from"));
            message.setTo(passwordResetToken.getEmail());

            String htmlContent = templateEngine.process("password-reset", ctx);
            message.setText(htmlContent, true); // true = isHtml

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new ServiceException(e);
        }

        return passwordResetToken;
    }

    @Override
    public User updateUser(UserUpdateRequest form, AuthorizedUser authorizedUser) {

        User user = userRepository.findOneForUpdateById(form.getId());
        user.setName(form.getName());
        user.setNickname(form.getNickname());
        user.setEmail(form.getEmail());
        user.setDescription(form.getDescription());
        user = userRepository.saveAndFlush(user);
        return user;
    }

    @Override
    public User updateProfile(ProfileUpdateRequest request, AuthorizedUser updatedBy) {
        User user = userRepository.findOne(request.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("The user does not exist");
        }

        User duplicate;
        if (!ObjectUtils.nullSafeEquals(request.getEmail(), user.getEmail())) {
            duplicate = userRepository.findOneByEmail(request.getEmail());
            if (duplicate != null) {
                throw new DuplicateEmailException(request.getEmail());
            }
        }
        if (!ObjectUtils.nullSafeEquals(request.getLoginId(), user.getLoginId())) {
            duplicate = userRepository.findOneByLoginId(request.getLoginId());
            if (duplicate != null) {
                throw new DuplicateLoginIdException(request.getLoginId());
            }
        }

        user.setEmail(request.getEmail());
        user.setLoginId(request.getLoginId());
        user.setName(request.getName());
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(updatedBy.toString());
        return userRepository.save(user);
    }

    @Override
    public User updatePassword(PasswordUpdateRequest request, PasswordResetToken passwordResetToken) {
        User user = userRepository.findOneForUpdateById(request.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("The user does not exist");
        }
        PasswordEncoder passwordEncoder = new StandardPasswordEncoder();
        user.setLoginPassword(passwordEncoder.encode(request.getPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(passwordResetToken.getUser().toString());
        user = userRepository.saveAndFlush(user);

        passwordResetTokenRepository.delete(passwordResetToken);

        try {
            Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
            String blogTitle = blog.getTitle(LocaleContextHolder.getLocale().getLanguage());

            ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentContextPath();
            if (blog.isMultiLanguage()) {
                builder.path("/{language}");
            }
            builder.path("/login");

            Map<String, Object> urlVariables = new LinkedHashMap<>();
            urlVariables.put("language", request.getLanguage());
            urlVariables.put("token", passwordResetToken.getToken());
            String loginLink = builder.buildAndExpand(urlVariables).toString();

            Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("passwordResetToken", passwordResetToken);
            ctx.setVariable("resetLink", loginLink);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
            message.setFrom(mailProperties.getProperties().get("mail.from"));
            message.setTo(passwordResetToken.getEmail());

            String htmlContent = templateEngine.process("password-changed", ctx);
            message.setText(htmlContent, true); // true = isHtml

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new ServiceException(e);
        }

        return user;
    }

    @Override
    public User updatePassword(PasswordUpdateRequest request, AuthorizedUser updatedBy) {
        User user = userRepository.findOneForUpdateById(request.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("The user does not exist");
        }
        PasswordEncoder passwordEncoder = new StandardPasswordEncoder();
        user.setLoginPassword(passwordEncoder.encode(request.getPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(updatedBy.toString());
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    @Override
    public void bulkDeleteUser(UserBulkDeleteRequest deleteRequest) {
        List<User> users = userRepository.findAll(deleteRequest.getIds());
        userRepository.delete(users);
    }

    @Override
    public List<UserInvitation> inviteUsers(UserInvitationCreateRequest form, BindingResult result, AuthorizedUser authorizedUser) throws MessagingException {

        String[] recipients = StringUtils.commaDelimitedListToStringArray(form.getInvitees());
        LocalDateTime now = LocalDateTime.now();

        List<UserInvitation> invitations = new ArrayList<>();
        for (String recipient : recipients) {
            UserInvitation invitation = new UserInvitation();
            invitation.setEmail(recipient);
            invitation.setMessage(form.getMessage());
            invitation.setExpiredAt(now.plusHours(72));
            invitation.setCreatedAt(now);
            invitation.setCreatedBy(authorizedUser.toString());
            invitation.setUpdatedAt(now);
            invitation.setUpdatedBy(authorizedUser.toString());
            invitations.add(invitation);
        }
        userInvitationRepository.save(invitations);
        Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
        for (UserInvitation invitation : invitations) {
            String websiteTitle = blog.getTitle(LocaleContextHolder.getLocale().getLanguage());
            String signupLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/_admin/signup")
                    .queryParam("token", invitation.getToken())
                    .buildAndExpand().toString();

            final Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("websiteTitle", websiteTitle);
            ctx.setVariable("authorizedUser", authorizedUser);
            ctx.setVariable("signupLink", signupLink);
            ctx.setVariable("invitation", invitation);

            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
            message.setFrom(authorizedUser.getEmail());
            message.setTo(invitation.getEmail());

            final String htmlContent = templateEngine.process("user-invite", ctx);
            message.setText(htmlContent, true); // true = isHtml

            mailSender.send(mimeMessage);
        }

        return invitations;
    }

    @Override
    public UserInvitation inviteAgain(UserInvitationResendRequest form, BindingResult result, AuthorizedUser authorizedUser) throws MessagingException {
        LocalDateTime now = LocalDateTime.now();

        UserInvitation invitation = userInvitationRepository.findOneForUpdateByToken(form.getToken());
        invitation.setExpiredAt(now.plusHours(72));
        invitation.setUpdatedAt(now);
        invitation.setUpdatedBy(authorizedUser.toString());
        invitation = userInvitationRepository.saveAndFlush(invitation);

        Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
        String websiteTitle = blog.getTitle(LocaleContextHolder.getLocale().getLanguage());
        String signupLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/_admin/signup")
                .queryParam("token", invitation.getToken())
                .buildAndExpand().toString();

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("websiteTitle", websiteTitle);
        ctx.setVariable("authorizedUser", authorizedUser);
        ctx.setVariable("signupLink", signupLink);
        ctx.setVariable("invitation", invitation);
        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setFrom(authorizedUser.getEmail());
        message.setTo(invitation.getEmail());
        final String htmlContent = templateEngine.process("user-invite", ctx);
        message.setText(htmlContent, true); // true = isHtml
        mailSender.send(mimeMessage);
        return invitation;
    }

    @Override
    public UserInvitation deleteUserInvitation(UserInvitationDeleteRequest request) {
        UserInvitation invitation = userInvitationRepository.findOneForUpdateByToken(request.getToken());
        userInvitationRepository.delete(invitation);
        return invitation;
    }

    @Override
    public Page<User> getUsers(UserSearchRequest request) {
        Pageable pageable = new PageRequest(0, 10);
        return getUsers(request, pageable);
    }

    @Override
    public Page<User> getUsers(UserSearchRequest request, Pageable pageable) {
//		return userRepository.search(request, pageable);
        return userRepository.findAll(pageable);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public User getUserByLoginId(String loginId) {
        return userRepository.findOneByLoginId(loginId);
    }

    @Override
    public List<UserInvitation> getUserInvitations() {

        return userInvitationRepository.findAll(new Sort(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public PasswordResetToken getPasswordResetToken(String token) {

        return passwordResetTokenRepository.findOneByToken(token);
    }

}
