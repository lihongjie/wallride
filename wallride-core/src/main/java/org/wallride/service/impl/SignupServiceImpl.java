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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.wallride.domain.User;
import org.wallride.domain.UserInvitation;
import org.wallride.exception.DuplicateEmailException;
import org.wallride.exception.DuplicateLoginIdException;
import org.wallride.exception.ServiceException;
import org.wallride.model.SignupRequest;
import org.wallride.repository.UserInvitationRepository;
import org.wallride.repository.UserRepository;
import org.wallride.service.SignupService;
import org.wallride.support.AuthorizedUser;
import org.wallride.web.controller.guest.user.SignupCheckRequest;
import org.wallride.web.support.HttpForbiddenException;

import java.time.LocalDateTime;

@Service
public class SignupServiceImpl implements SignupService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserInvitationRepository userInvitationRepository;

	@Override
	public UserInvitation readUserInvitation(String token) {
		return userInvitationRepository.findOneByToken(token);
	}
	@Override
	public boolean validateInvitation(UserInvitation invitation) {
		if (invitation == null) {
			return false;
		}
		if (invitation.isAccepted()) {
			return false;
		}
		LocalDateTime now = LocalDateTime.now();
		if (now.isAfter(invitation.getExpiredAt())) {
			return false;
		}
		return true;
	}

	@Override
	public AuthorizedUser signup(SignupRequest request, User.Role role) throws ServiceException {
		return signup(request, role, null);
	}

	@Override
	public AuthorizedUser signup(SignupRequest request, User.Role role, String token) throws ServiceException {
		UserInvitation invitation = null;
		if (token != null) {
			invitation = userInvitationRepository.findOneForUpdateByToken(token);
			if (invitation == null) {
				throw new HttpForbiddenException();
			}
			if (!validateInvitation(invitation)) {
				throw new HttpForbiddenException();
			}
		}

		User duplicate;
		duplicate = userRepository.findOneByLoginId(request.getLoginId());
		if (duplicate != null) {
			throw new DuplicateLoginIdException(request.getLoginId());
		}
		duplicate = userRepository.findOneByEmail(request.getEmail());
		if (duplicate != null) {
			throw new DuplicateEmailException(request.getEmail());
		}

		LocalDateTime now = LocalDateTime.now();
		if (invitation != null) {
			invitation.setAccepted(true);
			invitation.setAcceptedAt(now);
			userInvitationRepository.saveAndFlush(invitation);
		}

		User user = new User();
		user.setLoginId(request.getLoginId());
		StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();
		user.setLoginPassword(passwordEncoder.encode(request.getLoginPassword()));
		user.getName().setFirstName(request.getName().getFirstName());
		user.getName().setLastName(request.getName().getLastName());
		user.setEmail(request.getEmail());
		user.getRoles().add(role);
		user.setCreatedAt(now);
		user.setUpdatedAt(now);
		user = userRepository.saveAndFlush(user);

		AuthorizedUser authorizedUser = new AuthorizedUser(user);
//		Authentication auth = new UsernamePasswordAuthenticationToken(authorizedUser, null, authorizedUser.getAuthorities());
//		SecurityContextHolder.getContext().setAuthentication(auth);

		return authorizedUser;
	}

	@Override
	public User signupCheckUsername(SignupCheckRequest checkRequest) {
		User user = userRepository.findOneByLoginId(checkRequest.getValue());
		if (user == null) {
			throw new DuplicateLoginIdException("");
		}
		return user;
	}

	@Override
	public User signupCheckEmail(SignupCheckRequest checkRequest) {
		User user = userRepository.findOneByEmail(checkRequest.getValue());
		if (user == null) {
			throw new DuplicateEmailException("");
		}
		return user;
	}
}
