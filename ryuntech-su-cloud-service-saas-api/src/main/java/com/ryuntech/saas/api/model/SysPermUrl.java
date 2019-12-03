package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 资源URL表
 * </p>
 *
 * @author antu
 * @since 2019-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysPermUrl implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("perm_id")
    private String permId;

    @TableField("url")
    private String url;

    /**
     * 菜单类型（1.菜单 2.按钮 3.接口 4.特殊）
     */
    @TableField("res_type")
    private Integer resType;


}
