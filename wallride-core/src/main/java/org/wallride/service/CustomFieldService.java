package org.wallride.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.wallride.domain.CustomField;
import org.wallride.model.CustomFieldBulkDeleteRequest;
import org.wallride.model.CustomFieldCreateRequest;
import org.wallride.model.CustomFieldSearchRequest;
import org.wallride.model.CustomFieldUpdateRequest;
import org.wallride.support.AuthorizedUser;

import java.util.List;
import java.util.SortedSet;

public interface CustomFieldService {

	CustomField createCustomField(CustomFieldCreateRequest request, AuthorizedUser authorizedUser);

	CustomField updateCustomField(CustomFieldUpdateRequest request, AuthorizedUser authorizedUser);

	void updateCustomFieldOrder(List<Long> data, String language, BindingResult result);

	List<CustomField> bulkDeleteCustomField(CustomFieldBulkDeleteRequest bulkDeleteRequest);
	
	CustomField getCustomFieldById(long id, String language);

	CustomField getCustomFieldByName(String name, String language);

	CustomField getCustomFieldByCode(String code, String language);

	Page<CustomField> getCustomFields(CustomFieldSearchRequest request);

	Page<CustomField> getCustomFields(CustomFieldSearchRequest request, Pageable pageable);

	SortedSet<CustomField> getAllCustomFields();

	SortedSet<CustomField> getAllCustomFields(String language);
}
