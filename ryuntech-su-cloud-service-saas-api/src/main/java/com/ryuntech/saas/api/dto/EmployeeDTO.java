package com.ryuntech.saas.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Id;

/**
 * @author liugg
 */
@Data
@Accessors(chain = true)
public class EmployeeDTO {
    /**
     * 主键
     */
    @Id
    private String employeeId;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 对应用户ID
     */
    private String userId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 公司名
     */
    private String companyName;


    /**
     * 公司ID
     */
    private String companyId;

    /**
     * 部门
     */
    private String departmentName;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * email
     */
    private String email;


    /**
     * 状态
     */
    private Integer status;

    /**
     * 角色名
     */
    private String rname;

}
