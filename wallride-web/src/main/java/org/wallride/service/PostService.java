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

import org.wallride.domain.Language;
import org.wallride.domain.PopularPost;
import org.wallride.domain.Tag;
import org.wallride.model.PostSearchRequest;

import java.util.List;
import java.util.SortedSet;

public interface PostService {

	List<Post> publishScheduledPosts();

	void updatePostViews();

	void updatePopularPosts(Language language, PopularPost.Type type, int maxRank);

	Page<Post> getPosts(PostSearchRequest request);

	Page<Post> getPosts(PostSearchRequest request, Pageable pageable);

	SortedSet<PopularPost> getPopularPosts(String language, PopularPost.Type type);

	Post getPostById(long id, String language);

	Page<Post> getPostsByTag(Tag tag, Pageable pageable);
}
