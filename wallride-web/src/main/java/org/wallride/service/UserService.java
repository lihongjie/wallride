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
import org.springframework.validation.BindingResult;
import org.wallride.domain.PasswordResetToken;
import org.wallride.domain.User;
import org.wallride.domain.UserInvitation;
import org.wallride.model.*;
import org.wallride.support.AuthorizedUser;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {

	PasswordResetToken createPasswordResetToken(PasswordResetTokenCreateRequest request);

	User updateUser(UserUpdateRequest form, AuthorizedUser authorizedUser);

	User updateProfile(ProfileUpdateRequest request, AuthorizedUser updatedBy);

	User updatePassword(PasswordUpdateRequest request, PasswordResetToken passwordResetToken);

	User updatePassword(PasswordUpdateRequest request, AuthorizedUser updatedBy);

	void bulkDeleteUser(UserBulkDeleteRequest deleteRequest);

	List<UserInvitation> inviteUsers(UserInvitationCreateRequest form, BindingResult result, AuthorizedUser authorizedUser) throws MessagingException;

	UserInvitation inviteAgain(UserInvitationResendRequest form, BindingResult result, AuthorizedUser authorizedUser) throws MessagingException;

	UserInvitation deleteUserInvitation(UserInvitationDeleteRequest request);

	Page<User> getUsers(UserSearchRequest request);

	Page<User> getUsers(UserSearchRequest request, Pageable pageable);

	User getUserById(Long id);

	User getUserByLoginId(String loginId);

	List<UserInvitation> getUserInvitations();

	PasswordResetToken getPasswordResetToken(String token);
}
