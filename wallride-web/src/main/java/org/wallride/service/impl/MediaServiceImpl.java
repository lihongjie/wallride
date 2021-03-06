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

import com.github.lihongjie.core.base.util.SequenceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wallride.autoconfigure.WallRideProperties;
import org.wallride.domain.Media;
import org.wallride.mapper.MediaMapper;
import org.wallride.service.MediaService;
import org.wallride.support.ExtendedResourceUtils;

import java.io.IOException;
import java.util.List;

@Service
public class MediaServiceImpl implements MediaService {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private WallRideProperties wallRideProperties;

	@Autowired
	private MediaMapper mediaMapper;
	@Override
	public Media createMedia(MultipartFile file) {

		Media media = new Media();
		media.setId(SequenceUtils.nexSeqId());
		media.setMimeType(file.getContentType());
		media.setOriginalName(file.getOriginalFilename());
		mediaMapper.insertSelective(media);

//		Blog blog = blogService.getBlogById(Blog.DEFAULT_ID);
		try {
			Resource prefix = resourceLoader.getResource(wallRideProperties.getMediaLocation());
			Resource resource = prefix.createRelative(media.getId());
//			AmazonS3ResourceUtils.writeMultipartFile(file, resource);
			ExtendedResourceUtils.write(resource, file);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}

		return media;
	}
	@Override
	public List<Media> getAllMedias() {
		return mediaRepository.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "createdAt")));
	}
	@Override
	public Media getMedia(String id) {
		return mediaRepository.findOneById(id);
	}

	@Override
	public void deleteMedia(String id) {

		mediaRepository.delete(id);
	}
}
