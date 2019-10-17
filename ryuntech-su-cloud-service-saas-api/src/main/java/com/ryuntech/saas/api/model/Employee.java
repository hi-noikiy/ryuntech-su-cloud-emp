package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    *
    * </p>
*
* @author antu
* @since 2019-10-15
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_employee")
    public class Employee extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 职工编号
     */
    @TableId("EMPLOYEE_ID")
    private String employeeId;

    /**
     * 用户编号
     */
    @TableField("USER_ID")
    private String userId;

    /**
     * 职工姓名
     */
    @TableField("NAME")
    private String name;

    /**
     * 公司编号
     */
    @TableField("COMPANY_ID")
    private String companyId;

    /**
     * 所属部门ID
     */
    @TableField("DEPARTMENT_ID")
    private String departmentId;

    /**
     * 手机号（和登录手机号相同）
     */
    @TableField("MOBILE")
    private String mobile;

    /**
     * 邮箱
     */
    @TableField("EMAIL")
    private String email;

    /**
     * 帐号状态 0-正常 1-禁用
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 公司姓名
     */
    @TableField("COMPANY_NAME")
    private String companyName;

    @TableField("UPDATED_AT")
    private LocalDateTime updatedAt;

    @TableField("CREATED_AT")
    private LocalDateTime createdAt;


}
