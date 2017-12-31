package org.wallride.domain;

import java.time.LocalDateTime;

public class UserLoginHistory {

    private Long userLoginId;

    private LocalDateTime fromDate;

    private LocalDateTime thruDate;

    private String passwordUsed;

    private Long successfulLogin;

    public Long getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(Long userLoginId) {
        this.userLoginId = userLoginId;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDateTime thruDate) {
        this.thruDate = thruDate;
    }

    public String getPasswordUsed() {
        return passwordUsed;
    }

    public void setPasswordUsed(String passwordUsed) {
        this.passwordUsed = passwordUsed;
    }

    public Long getSuccessfulLogin() {
        return successfulLogin;
    }

    public void setSuccessfulLogin(Long successfulLogin) {
        this.successfulLogin = successfulLogin;
    }
}
