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

package org.wallride.web.controller.admin.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wallride.autoconfigure.WallRideProperties;
import org.wallride.domain.Media;
import org.wallride.service.MediaService;

import java.util.List;

@Controller
@RequestMapping("/{language}/media")
public class MediaController {

	@Autowired
	private MediaService mediaService;

	@Autowired
	private WallRideProperties wallRideProperties;

	@PostMapping(value = "/create.json")
	public ResponseEntity create(@RequestParam MultipartFile file) {

		Media media = mediaService.createMedia(file);

		return ResponseEntity
				.ok(new MediaCreatedModel(media, wallRideProperties));

	}

	@GetMapping(value = "/index")
	public ResponseEntity index() {
		List<Media> medias = mediaService.getAllMedias();
		MediaIndexModel[] models = new MediaIndexModel[medias.size()];
		for (int i = 0; i < medias.size(); i++) {
			models[i] = new MediaIndexModel(medias.get(i), wallRideProperties);
		}
		return ResponseEntity.ok(models);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity delete(@PathVariable String id) {

		mediaService.deleteMedia(id);
		return ResponseEntity
				.status(HttpStatus.OK)
				.build();
	}
}
