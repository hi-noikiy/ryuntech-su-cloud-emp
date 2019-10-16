package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
    public class Employee implements Serializable {

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
            * 公司姓名
            */
        @TableField("COMPANY_NAME")
    private String companyName;


}
