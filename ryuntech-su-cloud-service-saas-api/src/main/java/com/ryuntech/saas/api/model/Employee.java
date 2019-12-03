package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

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
     * 主键
     */
    @TableId("employee_id")
    private String employeeId;

    /**
     * 职工编号
     */
    @TableField("code")
    private String code;

    /**
     * 用户ID
     */
    @TableField("sys_user_id")
    private String sysUserId;

    /**
     * 职工姓名
     */
    @TableField("name")
    private String name;

    /**
     * 公司ID
     */
    @TableField("company_id")
    private String companyId;

    /**
     * 公司姓名
     */
    @TableField("company_name")
    private String companyName;

    /**
     * 所属部门ID
     */
    @TableField("department_id")
    private String departmentId;

    /**
     * 所属部门名称
     */
    @TableField("DEPARTMENT_NAME")
    private String departmentName;

    /**
     * 手机号（和登录手机号相同）
     */
    @TableField("MOBILE")
    private String mobile;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 帐号状态 0-正常 1-禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * 数据权限（1=本人2=本部门及下属部门3=全部4=指定部门）
     */
    @TableField("data_type")
    private Integer dataType;

    /**
     * 可操作部门ID（多个英文逗号隔开）
     */
    @TableField("data_department_id")
    private String dataDepartmentId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("updated_at")
    private Date updatedAt;


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("created_at")
    private Date createdAt;


}
