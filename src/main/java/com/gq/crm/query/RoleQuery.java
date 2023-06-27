package com.gq.crm.query;

import com.gq.base.BaseQuery;

/**
 * @date 2023/6/16
 */


public class RoleQuery extends BaseQuery {
    private String roleName; // 角色名称

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
