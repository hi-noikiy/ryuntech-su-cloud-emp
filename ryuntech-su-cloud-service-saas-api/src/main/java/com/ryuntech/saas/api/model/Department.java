package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("ryn_department")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("department_id")
    private String departmentId;

    /**
     * 所属公司ID
     */
    @TableField("company_id")
    private String companyId;

    /**
     * 部门名称
     */
    @TableField("department_name")
    private String departmentName;

    /**
     * 部门级别
     */
    @TableField("level")
    private String level;

    /**
     * 父级ID
     */
    @TableField("pid")
    private String pid;

    @TableField("updated_at")
    @JsonIgnore
    private Date updatedAt;

    @TableField("created_at")
    @JsonIgnore
    private Date createdAt;


}
