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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wallride.domain.Category;
import org.wallride.domain.Category_;
import org.wallride.model.CategoryCreateRequest;
import org.wallride.model.CategoryResponse;
import org.wallride.model.CategorySearchRequest;
import org.wallride.model.CategoryUpdateRequest;
import org.wallride.repository.CategoryRepository;
import org.wallride.repository.CategorySpecifications;
import org.wallride.service.CategoryService;
import org.wallride.support.AuthorizedUser;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category createCategory(CategoryCreateRequest request, AuthorizedUser authorizedUser) {
		Category category = new Category();

		Category parent = null;
		if (request.getParentId() != null) {
			parent = categoryRepository.findOneByIdAndLanguage(request.getParentId(), request.getLanguage());
		}

//		int rgt = 0;
//		if (parent == null) {
//			rgt = categoryRepository.findMaxRgt();
//			rgt++;
//		}
//		else {
//			rgt = parent.getRgt();
//			categoryRepository.unshiftRgt(rgt);
//			categoryRepository.unshiftLft(rgt);
//		}

		category.setParent(parent);
		String code = request.getCode();

//		code = new CodeFormatter().parse(request.getName(), LocaleContextHolder.getLocale());

		String userName = authorizedUser.getUsername();
		category.setCreatedBy(userName);
		category.setUpdatedBy(userName);
		category.setCode(code);
		category.setName(request.getName());
		category.setDescription(request.getDescription());
//		category.setLft(rgt);
//		category.setRgt(rgt + 1);
		category.setLanguage(request.getLanguage());

		return categoryRepository.save(category);
	}
	@Override
	public Category updateCategory(CategoryUpdateRequest request, AuthorizedUser authorizedUser) {

		Category category = categoryRepository.findOneByIdAndLanguage(request.getId(), request.getLanguage());
		Category parent = null;
		if (request.getParentId() != null) {
			parent = categoryRepository.findOneByIdAndLanguage(request.getParentId(), request.getLanguage());
		}

//		if (!(category.getParent() == null && parent == null) && !ObjectUtils.nullSafeEquals(category.getParent(), parent)) {
//			categoryRepository.shiftLftRgt(category.getLft(), category.getRgt());
//			categoryRepository.shiftRgt(category.getRgt());
//			categoryRepository.shiftLft(category.getRgt());
//
//			int rgt = 0;
//			if (parent == null) {
//				rgt = categoryRepository.findMaxRgt();
//				rgt++;
//			}
//			else {
//				rgt = parent.getRgt();
//				categoryRepository.unshiftRgt(rgt);
//				categoryRepository.unshiftLft(rgt);
//			}
//			category.setLft(rgt);
//			category.setRgt(rgt + 1);
//		}

		category.setParent(parent);
		String code = request.getCode();
//		if (code == null) {
//			try {
//				code = new CodeFormatter().parse(request.getName(), LocaleContextHolder.getLocale());
//			} catch (ParseException e) {
//				throw new ServiceException(e);
//			}
//		}
		category.setUpdatedBy(authorizedUser.getUsername());
		category.setCode(code);
		category.setName(request.getName());
		category.setDescription(request.getDescription());
		category.setLanguage(request.getLanguage());

		return categoryRepository.save(category);
	}
	@Override
	public void updateCategoryHierarchy(List<Map<String, Object>> data, String language) {
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> map = data.get(i);
			if (map.get("item_id") != null) {

				Category category = categoryRepository.findOneByIdAndLanguage(Long.parseLong((String) map.get("item_id")), language);
				if (category != null) {
					Category parent = null;
					if (map.get("parent_id") != null) {
						parent = categoryRepository.findOneByIdAndLanguage(Long.parseLong((String) map.get("parent_id")), language);
					}
					category.setParent(parent);
					category.setLft(((int) map.get("left")) - 1);
					category.setRgt(((int) map.get("right")) - 1);
					categoryRepository.save(category);
				}
			}
		}
	}

	@Transactional
	@Override
	public Category deleteCategory(Long id, String language) {

		Category category = categoryRepository.findOneByIdAndLanguage(id, language);
		Category parent = category.getParent();
		for (Category child : category.getChildren()) {
			child.setParent(parent);
			categoryRepository.save(child);
		}
		category.getChildren().clear();
		categoryRepository.delete(category);
		return category;
	}
	@Override
	public Category getCategoryById(Long id, String language) {
		return categoryRepository.findOneByIdAndLanguage(id, language);
	}
	@Override
	public Category getCategoryByCode(String code, String language) {
		return categoryRepository.findOneByCodeAndLanguage(code, language);
	}
	@Override
	public List<Category> getCategories(String language, boolean includeNoPosts) {
		if (includeNoPosts) {
			return categoryRepository.findAllDistinctByLanguageOrderByLftAsc(language);
		} else {
			return categoryRepository.findAll(CategorySpecifications.hasPosts(language), new Sort(Category_.lft.getName()));
		}
	}
	@Override
	public Page<Category> getCategories(CategorySearchRequest request) {
		Pageable pageable = new PageRequest(0, 10);
		return getCategories(request, pageable);
	}
	@Override
	public Page<Category> getCategories(CategorySearchRequest request, Pageable pageable) {
//		return categoryRepository.search(request, pageable);
		return categoryRepository.findAll(pageable);
	}
	@Override
	public List<CategoryResponse> categoryGroup() {
		return categoryRepository.findAllGroupByCateogry();
	}
}