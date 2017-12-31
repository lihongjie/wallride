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

import org.springframework.data.domain.Pageable;
import org.wallride.domain.Page;
import org.wallride.domain.Post;
import org.wallride.model.PageBulkDeleteRequest;
import org.wallride.model.PageCreateRequest;
import org.wallride.model.PageSearchRequest;
import org.wallride.model.PageUpdateRequest;
import org.wallride.support.AuthorizedUser;

import java.util.List;
import java.util.Map;

public interface PageService {

	Page createPage(PageCreateRequest request, Post.Status status, AuthorizedUser authorizedUser);

	Page savePageAsDraft(PageUpdateRequest request, AuthorizedUser authorizedUser);

	Page savePages(PageUpdateRequest request, AuthorizedUser authorizedUser);

	Page savePage(PageUpdateRequest request, AuthorizedUser authorizedUser);

	void updatePageHierarchy(List<Map<String, Object>> data, String language);

	Page deletePage(Long id, String language);

	List<Page> bulkDeletePage(PageBulkDeleteRequest bulkDeleteRequest);

	List<Long> getPageIds(PageSearchRequest request);

	org.springframework.data.domain.Page<Page> getPages(PageSearchRequest request);

	org.springframework.data.domain.Page<Page> getPages(PageSearchRequest request, Pageable pageable);

	List<Page> getPathPages(Page page);

	List<Page> getPathPages(Page page, boolean includeUnpublished);

	List<Page> getChildPages(Page page);

	List<Page> getChildPages(Page page, boolean includeUnpublished);

	List<Page> getSiblingPages(Page page);

	List<Page> getSiblingPages(Page page, boolean includeUnpublished);

	Page getPageById(Long id);

	Page getPageById(Long id, String language);

	Page getPageByCode(String code, String language);

	Page getDraftById(Long id);

	long countPages(String language);

	long countPagesByStatus(Post.Status status, String language);
}
