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
    @TableName("ryn_department")
    public class Department extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId("DEPARTMENT_ID")
    private String departmentId;

    /**
     * 所属公司ID
     */
    @TableField("COMPANY_ID")
    private String companyId;

    /**
     * 部门名称
     */
    @TableField("DEPARTMENT_NAME")
    private String departmentName;

    /**
     * 部门级别
     */
    @TableField("LEVEL")
    private String level;

    /**
     * 父级ID
     */
    @TableField("PID")
    private String pid;

    @TableField("UPDATED_AT")
    private LocalDateTime updatedAt;

    @TableField("CREATED_AT")
    private LocalDateTime createdAt;


}
