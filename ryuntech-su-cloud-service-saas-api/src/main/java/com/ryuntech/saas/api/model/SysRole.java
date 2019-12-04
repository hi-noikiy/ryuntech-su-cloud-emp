package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author antu
 * @since 2019-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @TableId("rid")
    private String rid;

    /**
     * 是否为超级管理员（0=否1=是）
     */
    @TableField("is_admin")
    private Integer isAdmin;

    /**
     * 是否预设角色（0=否1=是）
     */
    @TableField("is_preset")
    private Integer isPreset;

    /**
     * 角色名，用于显示
     */
    @TableField("rname")
    private String rname;

    /**
     * 角色描述
     */
    @TableField("rdesc")
    private String rdesc;

    /**
     * 所属公司
     */
    @TableField("company_id")
    private String companyId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "created")
    private Date created;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "updated")
    private Date updated;


}
