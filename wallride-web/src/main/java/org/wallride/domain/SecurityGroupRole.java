package org.wallride.domain;

import com.github.lihongjie.core.entity.GeneralEntity;

public class SecurityGroupRole extends GeneralEntity {

    private String groupId;

    private String roleId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
