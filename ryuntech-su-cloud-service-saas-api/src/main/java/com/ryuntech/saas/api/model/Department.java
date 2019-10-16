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
    @TableName("ryn_department")
    public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

        @TableField("DEPARTMENT_ID")
    private String departmentId;

        @TableField("COMPANY_ID")
    private String companyId;

        @TableField("DEPARTMENT_NAME")
    private String departmentName;

        @TableField("LEVEL")
    private String level;

        @TableField("PID")
    private String pid;


}
