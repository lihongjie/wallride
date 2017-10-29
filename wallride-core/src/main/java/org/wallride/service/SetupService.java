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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.autoconfigure.WallRideCacheConfiguration;
import org.wallride.domain.Blog;
import org.wallride.domain.BlogLanguage;
import org.wallride.domain.User;
import org.wallride.model.SetupRequest;
import org.wallride.repository.BlogRepository;
import org.wallride.repository.UserRepository;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class)
public class SetupService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BlogRepository blogRepository;

	@CacheEvict(value = {WallRideCacheConfiguration.USER_CACHE, WallRideCacheConfiguration.BLOG_CACHE}, allEntries = true)
	public User setup(SetupRequest request) {

		User user = new User();
		user.setLoginId(request.getLoginId());
		StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();
		user.setLoginPassword(passwordEncoder.encode(request.getLoginPassword()));
		user.getName().setFirstName(request.getName().getFirstName());
		user.getName().setLastName(request.getName().getLastName());
		user.setEmail(request.getEmail());
		user.getRoles().add(User.Role.ADMIN);
		user = userRepository.saveAndFlush(user);

		String userName = user.toString();
		Blog blog = new Blog();
		blog.setCode("default");
		blog.setDefaultLanguage(request.getDefaultLanguage());
		blog.setCreatedBy(user.toString());
		blog.setUpdatedBy(user.toString());
		BlogLanguage defaultLanguage = new BlogLanguage();
		defaultLanguage.setBlog(blog);
		defaultLanguage.setLanguage(request.getDefaultLanguage());
		defaultLanguage.setTitle(request.getWebsiteTitle());
		defaultLanguage.setCreatedBy(userName);
		defaultLanguage.setUpdatedBy(userName);

		Set<BlogLanguage> blogLanguages = new HashSet<>();
		blogLanguages.add(defaultLanguage);

		for (String language : request.getLanguages()) {
			BlogLanguage blogLanguage = new BlogLanguage();
			blogLanguage.setBlog(blog);
			blogLanguage.setLanguage(language);
			blogLanguage.setTitle(request.getWebsiteTitle());
			blogLanguage.setCreatedBy(userName);
			blogLanguage.setUpdatedBy(userName);

			blogLanguages.add(blogLanguage);
		}
		blog.setLanguages(blogLanguages);
		blogRepository.saveAndFlush(blog);
		return user;
	}
}
