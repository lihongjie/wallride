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

public class UserLogin extends GeneralEntity {

    private String userLoginId;

    private String userId;

    private String currentPassword;

    private String passwordHint;

    private String isSystem;

    private String enabled;

    private String hasLoggedOut;

    private String requirePasswordChange;

    private String lastCurrencyUom;

    private String lastLocale;

    private String lasTimeZone;

    private String lastHostAddress;

    private LocalDateTime disabledDateTime;

    private BigDecimal successiveFailedLogins;

    private UserLoginHistory userLoginHistory;

    private UserLoginPasswordHistory userLoginPasswordHistory;

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getPasswordHint() {
        return passwordHint;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getHasLoggedOut() {
        return hasLoggedOut;
    }

    public void setHasLoggedOut(String hasLoggedOut) {
        this.hasLoggedOut = hasLoggedOut;
    }

    public String getRequirePasswordChange() {
        return requirePasswordChange;
    }

    public void setRequirePasswordChange(String requirePasswordChange) {
        this.requirePasswordChange = requirePasswordChange;
    }

    public String getLastCurrencyUom() {
        return lastCurrencyUom;
    }

    public void setLastCurrencyUom(String lastCurrencyUom) {
        this.lastCurrencyUom = lastCurrencyUom;
    }

    public String getLastLocale() {
        return lastLocale;
    }

    public void setLastLocale(String lastLocale) {
        this.lastLocale = lastLocale;
    }

    public String getLasTimeZone() {
        return lasTimeZone;
    }

    public void setLasTimeZone(String lasTimeZone) {
        this.lasTimeZone = lasTimeZone;
    }

    public String getLastHostAddress() {
        return lastHostAddress;
    }

    public void setLastHostAddress(String lastHostAddress) {
        this.lastHostAddress = lastHostAddress;
    }

    public LocalDateTime getDisabledDateTime() {
        return disabledDateTime;
    }

    public void setDisabledDateTime(LocalDateTime disabledDateTime) {
        this.disabledDateTime = disabledDateTime;
    }

    public BigDecimal getSuccessiveFailedLogins() {
        return successiveFailedLogins;
    }

    public void setSuccessiveFailedLogins(BigDecimal successiveFailedLogins) {
        this.successiveFailedLogins = successiveFailedLogins;
    }

    public UserLoginHistory getUserLoginHistory() {
        return userLoginHistory;
    }

    public void setUserLoginHistory(UserLoginHistory userLoginHistory) {
        this.userLoginHistory = userLoginHistory;
    }

    public UserLoginPasswordHistory getUserLoginPasswordHistory() {
        return userLoginPasswordHistory;
    }

    public void setUserLoginPasswordHistory(UserLoginPasswordHistory userLoginPasswordHistory) {
        this.userLoginPasswordHistory = userLoginPasswordHistory;
    }
}
