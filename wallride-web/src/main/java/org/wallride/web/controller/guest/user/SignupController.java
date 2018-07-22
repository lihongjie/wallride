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

package org.wallride.web.controller.guest.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.wallride.domain.User;
import org.wallride.service.SignupService;

import javax.validation.Valid;

@Controller
@RequestMapping
public class SignupController {

	public static final String FORM_MODEL_KEY = "form";

	public static final String ERRORS_MODEL_KEY = BindingResult.MODEL_KEY_PREFIX + FORM_MODEL_KEY;

	@Autowired
	private SignupService signupService;

	@GetMapping("/join")
	public String init(Model model) {
		SignupForm form = new SignupForm();
		model.addAttribute(FORM_MODEL_KEY, form);
		return edit(model);
	}

	@RequestMapping(method = RequestMethod.GET, params = "step.edit")
	public String edit(Model model) {
		return "user/signup";
	}

	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity signup(SignupForm form) {

		signupService.signup(form.toSignupRequest(), User.Role.VIEWER);
		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

	@PostMapping(value = "/signup_check/username")
	public ResponseEntity signup_check_name(@Valid SignupCheckRequest checkRequest, BindingResult result) {
		if (result.hasErrors()) {

		}
		signupService.signupCheckUsername(checkRequest);
		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

	@PostMapping(value = "/signup_check/email")
	public ResponseEntity signup_check_email(@Valid SignupCheckRequest checkRequest, BindingResult result) {

		if (result.hasErrors()) {

		}
		signupService.signupCheckEmail(checkRequest);
		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}
}
