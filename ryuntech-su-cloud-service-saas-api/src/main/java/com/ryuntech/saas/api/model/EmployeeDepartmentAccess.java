package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
    @TableName("ryn_employee_department_access")
    public class EmployeeDepartmentAccess extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 所属公司ID
     */
    @TableField("company_id")
    private String companyId;

    /**
     * 职员ID
     */
    @TableField("employee_id")
    private String departmentName;

    /**
     * 部门ID
     */
    @TableField("department_id")
    private String departmentId;


    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField("created_at")
    private LocalDateTime createdAt;


}
