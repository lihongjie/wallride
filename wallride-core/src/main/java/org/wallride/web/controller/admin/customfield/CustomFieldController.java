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

package org.wallride.web.controller.admin.customfield;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.wallride.domain.CustomField;
import org.wallride.exception.DuplicateCodeException;
import org.wallride.exception.EmptyCodeException;
import org.wallride.service.CustomFieldService;
import org.wallride.support.AuthorizedUser;
import org.wallride.web.support.ControllerUtils;
import org.wallride.web.support.Pagination;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/{language}/customfields")
public class CustomFieldController {

    @Autowired
    private CustomFieldService customfieldService;

    @ModelAttribute("form")
    public CustomFieldSearchForm setupCustomfieldSearchForm() {
        return new CustomFieldSearchForm();
    }

    @ModelAttribute("query")
    public String query(@RequestParam(required = false) String query) {
        return query;
    }

    @GetMapping(value = "/index")
    public String index() {
        return "customfield/index";
    }
    @GetMapping
    public String search(
            @PathVariable String language,
            @Validated CustomFieldSearchForm form,
            BindingResult result,
            @PageableDefault Pageable pageable,
            Model model,
            HttpServletRequest servletRequest) {

        Page<CustomField> customFields = customfieldService.getCustomFields(form.toCustomFieldSearchRequest(), pageable);

        model.addAttribute("customFields", customFields);
        model.addAttribute("pageable", pageable);
        model.addAttribute("pagination", new Pagination<>(customFields, servletRequest));
        return "customfield/customfields";
    }

    @RequestMapping(value="/{language}/customfields", method= RequestMethod.PUT, consumes= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody CustomFieldIndexModel sort(@PathVariable String language, @RequestBody List<String> ids, BindingResult result) {
        List<Long> idList = ids.stream().map(Long::valueOf).collect(Collectors.toList());
        customfieldService.updateCustomFieldOrder(idList, language, result);
        return new CustomFieldIndexModel(idList, result);
    }

    @GetMapping(value = "/create")
    public String create() {
        return "customfield/create";
    }

    @PostMapping
    public String save(@PathVariable String language, @Validated CustomFieldCreateForm form, AuthorizedUser authorizedUser) {

        customfieldService.createCustomField(form.buildCustomFieldCreateRequest(), authorizedUser);
        return "redirect:/_admin/{language}/customfields/index";
    }

    @DeleteMapping
    public ResponseEntity delete(
            @Valid @ModelAttribute("form") CustomFieldBulkDeleteForm form,
            BindingResult errors,
            String query,
            AuthorizedUser authorizedUser,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("query", query);

        customfieldService.bulkDeleteCustomField(form.buildCustomFieldBulkDeleteRequest());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
