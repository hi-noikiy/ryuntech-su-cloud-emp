package com.ryuntech.saas.api.model;

import com.ryuntech.common.model.BaseModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wanh
 * @date 2019-10-31
 */

public class UserRoleLimit extends BaseModel {

    /**
     * 用户所属公司
     */
    private String company;

    /**
     * 用户角色
     */
    private Set<String> userRoles;

    /**
     * 用户权限
     */
    private Set<String> userLimit;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Set<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<String> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public String toString() {
        return "UserRoleLimit{" +
                "company='" + company + '\'' +
                ", userRoles=" + userRoles +
                ", userLimit=" + userLimit +
                '}';
    }

    public Set<String> getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(Set<String> userLimit) {
        this.userLimit = userLimit;
    }
}
