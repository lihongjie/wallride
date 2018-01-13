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
import org.wallride.domain.Category;
import org.wallride.domain.User;
import org.wallride.model.CategoryCreateRequest;
import org.wallride.model.CategoryResponse;
import org.wallride.model.CategorySearchRequest;
import org.wallride.model.CategoryUpdateRequest;
import org.wallride.support.AuthorizedUser;

import java.util.List;
import java.util.Map;

public interface CategoryService {

	Category createCategory(CategoryCreateRequest request, AuthorizedUser authorizedUser);

	Category updateCategory(CategoryUpdateRequest request, AuthorizedUser authorizedUser);

	void updateCategoryHierarchy(List<Map<String, Object>> data, String language);

	Category deleteCategory(Long id, String language);

	Category getCategoryById(Long id, String language);

	Category getCategoryByCode(String code, String language);

	List<Category> getCategories(String language, boolean includeNoPosts);

	Page<Category> getCategories(CategorySearchRequest request) ;

	Page<Category> getCategories(CategorySearchRequest request, Pageable pageable);

	List<CategoryResponse> categoryGroup();

	long countByUser(User user);

	List<Category> getCategories(User user);
}
