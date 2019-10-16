package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableName("ryn_employee_department")
    public class EmployeeDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

        @TableField("ID")
    private String id;

        @TableField("DEPARTMENT_ID")
    private String departmentId;

        @TableField("EMPLOYEE_ID")
    private String employeeId;


}
