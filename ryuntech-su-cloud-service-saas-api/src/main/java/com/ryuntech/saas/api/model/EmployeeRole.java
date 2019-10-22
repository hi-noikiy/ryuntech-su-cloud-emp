package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
* <p>
    * 职员-角色关联表
    * </p>
*
* @author antu
* @since 2019-10-17
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_employee_role")
    public class EmployeeRole extends BaseModel {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    private String id;

            /**
            * 职员ID
            */
    private String employeeId;

            /**
            * 角色ID
            */
    private String roleId;

            /**
            * 更新时间
            */
    private Date updatedAt;

            /**
            * 创建时间
            */
    private Date createdAt;


}
