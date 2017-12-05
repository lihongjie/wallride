package org.wallride.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.wallride.autoconfigure.WallRideCacheConfiguration;
import org.wallride.domain.CustomField;
import org.wallride.domain.CustomFieldOption;
import org.wallride.exception.DuplicateCodeException;
import org.wallride.exception.EmptyCodeException;
import org.wallride.model.CustomFieldBulkDeleteRequest;
import org.wallride.model.CustomFieldCreateRequest;
import org.wallride.model.CustomFieldSearchRequest;
import org.wallride.model.CustomFieldUpdateRequest;
import org.wallride.repository.CustomFieldRepository;
import org.wallride.support.AuthorizedUser;

import java.util.*;

@Service
@Transactional(rollbackFor=Exception.class)
public class CustomFieldService {

	private static Logger logger = LoggerFactory.getLogger(CustomFieldService.class);

	@Autowired
	private CustomFieldRepository customFieldRepository;

	@CacheEvict(value = WallRideCacheConfiguration.CUSTOM_FIELD_CACHE, allEntries = true)
	public CustomField createCustomField(CustomFieldCreateRequest request, AuthorizedUser authorizedUser) {
		CustomField customField = new CustomField();
		CustomField duplicate = customFieldRepository.findOneByCodeAndLanguage(request.getCode(), request.getLanguage());
		if (duplicate != null) {
			throw new DuplicateCodeException(request.getCode());
		}
		customField.setIdx(customFieldRepository.findMaxIdxByLanguage(request.getLanguage()) + 1);
		customField.setName(request.getName());
		customField.setCode(request.getCode());
		customField.setDescription(request.getDescription());
		customField.setFieldType(request.getType());
		customField.setLanguage(request.getLanguage());
		customField.getOptions().clear();
		if (!CollectionUtils.isEmpty(request.getOptions())) {
			request.getOptions().stream().forEach(optionName -> {
				CustomFieldOption option = new CustomFieldOption();
				option.setName(optionName);
				option.setLanguage(request.getLanguage());
				customField.getOptions().add(option);
			});
		}
		return customFieldRepository.save(customField);
	}

	public CustomField updateCustomField(CustomFieldUpdateRequest request, AuthorizedUser authorizedUser) {
		CustomField customField = customFieldRepository.findOneForUpdateById(request.getId());
		if (customField == null) {
			throw new EmptyCodeException(request.getName());
		}
		customField.setName(request.getName());
		customField.setCode(request.getCode());
		customField.setDescription(request.getDescription());
		customField.setFieldType(request.getType());
		customField.setLanguage(request.getLanguage());
		customField.getOptions().clear();
		if (!CollectionUtils.isEmpty(request.getOptions())) {
			request.getOptions().stream().forEach(optionName -> {
				CustomFieldOption option = new CustomFieldOption();
				option.setName(optionName);
				option.setLanguage(request.getLanguage());
				customField.getOptions().add(option);
			});
		}
		return customFieldRepository.save(customField);
	}

	@CacheEvict(value = WallRideCacheConfiguration.CUSTOM_FIELD_CACHE, allEntries = true)
	public void updateCustomFieldOrder(List<Long> data, String language, BindingResult result) {
		customFieldRepository.updateNullByLanguage(language);
		List<CustomField> customFields = customFieldRepository.findAllByLanguage(language);

		Map<Long, CustomField> fieldMap = new LinkedHashMap<>();
		customFields.stream().forEach(customField -> {
			fieldMap.put(customField.getId(), customField);
		});
		for (int i = 0; i < data.size(); i++) {
			CustomField customField = fieldMap.get(data.get(i));
			customField.setIdx(i + 1);
			customFieldRepository.save(customField);
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List<CustomField> bulkDeleteCustomField(CustomFieldBulkDeleteRequest bulkDeleteRequest) {
		List<CustomField> customFields = customFieldRepository.findAll(bulkDeleteRequest.getIds());
		customFieldRepository.deleteInBatch(customFields);
		return customFields;
	}
	
	public CustomField getCustomFieldById(long id, String language) {
		return customFieldRepository.findOneByIdAndLanguage(id, language);
	}

	public CustomField getCustomFieldByName(String name, String language) {
		return customFieldRepository.findOneByNameAndLanguage(name, language);
	}

	public CustomField getCustomFieldByCode(String code, String language) {
		return customFieldRepository.findOneByCodeAndLanguage(code, language);
	}

	public Page<CustomField> getCustomFields(CustomFieldSearchRequest request) {
		Pageable pageable = new PageRequest(0, 10);
		return getCustomFields(request, pageable);
	}

	public Page<CustomField> getCustomFields(CustomFieldSearchRequest request, Pageable pageable) {
//		return customFieldRepository.search(request, pageable);
		return customFieldRepository.findAll(pageable);
	}

	public SortedSet<CustomField> getAllCustomFields() {
		return new TreeSet<>(customFieldRepository.findAll());
	}

	public SortedSet<CustomField> getAllCustomFields(String language) {
		return new TreeSet<>(customFieldRepository.findAllByLanguage(language));
	}
}
