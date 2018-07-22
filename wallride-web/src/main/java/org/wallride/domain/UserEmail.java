package org.wallride.domain;

import com.github.lihongjie.core.entity.GeneralEntity;

import java.time.LocalDateTime;

public class UserEmail extends GeneralEntity {

    private String id;

    private String userId;

    private String eamilAddress;

    private LocalDateTime expiredDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEamilAddress() {
        return eamilAddress;
    }

    public void setEamilAddress(String eamilAddress) {
        this.eamilAddress = eamilAddress;
    }

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }
}
