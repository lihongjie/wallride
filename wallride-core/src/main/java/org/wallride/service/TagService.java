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
import org.wallride.domain.Tag;
import org.wallride.model.*;
import org.wallride.support.AuthorizedUser;

import java.util.List;

public interface TagService {


    Tag createTag(TagCreateRequest request, AuthorizedUser authorizedUser);

    Tag updateTag(TagUpdateRequest request, AuthorizedUser authorizedUser);

    Tag mergeTags(TagMergeRequest request, AuthorizedUser authorizedUser);

    List<Tag> bulkDeleteTag(TagBulkDeleteRequest bulkDeleteRequest);

    Tag getTagById(long id, String language);

    Tag getTagByName(String name, String language);

    List<Tag> getTags(String language);

    Page<Tag> getTags(TagSearchRequest request);

    Page<Tag> getTags(TagSearchRequest request, Pageable pageable);
}
