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

package org.wallride.web.controller.guest.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.wallride.model.CommentDeleteRequest;
import org.wallride.model.CommentUpdateRequest;
import org.wallride.service.CommentService;
import org.wallride.support.AuthorizedUser;

@RestController
@RequestMapping("/comments")
public class CommentController {

	@Autowired
	private CommentService commentService;



	@PutMapping(value = "/{id}")
	public ResponseEntity update(
			@PathVariable Long id,
			@Validated CommentForm form,
			BindingResult result,
			AuthorizedUser authorizedUser) {

		CommentUpdateRequest request = form.toCommentUpdateRequest(id);
//		commentService.updateComment(request, authorizedUser);

		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity delete(@PathVariable Long id) {

		CommentDeleteRequest request = new CommentDeleteRequest();
		request.setId(id);
		commentService.deleteComment(request);

		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}
}
