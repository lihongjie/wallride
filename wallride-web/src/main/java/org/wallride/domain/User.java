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

package org.wallride.domain;

import com.github.lihongjie.core.entity.GeneralEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class User extends GeneralEntity {

	private String id;

	private String firstName;

	private String lastName;

	private String nickname;

	private String gender;

	private LocalDateTime birthDate;

	private Double height;

	private Double weight;

	private String maritalStatus;

	private Double totalYearsWorkExperience;

	private String occupation;

	private BigDecimal yearsWithEmployer;

	private BigDecimal monthsWithEmployer;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDateTime getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDateTime birthDate) {
		this.birthDate = birthDate;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public Double getTotalYearsWorkExperience() {
		return totalYearsWorkExperience;
	}

	public void setTotalYearsWorkExperience(Double totalYearsWorkExperience) {
		this.totalYearsWorkExperience = totalYearsWorkExperience;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public BigDecimal getYearsWithEmployer() {
		return yearsWithEmployer;
	}

	public void setYearsWithEmployer(BigDecimal yearsWithEmployer) {
		this.yearsWithEmployer = yearsWithEmployer;
	}

	public BigDecimal getMonthsWithEmployer() {
		return monthsWithEmployer;
	}

	public void setMonthsWithEmployer(BigDecimal monthsWithEmployer) {
		this.monthsWithEmployer = monthsWithEmployer;
	}
}
