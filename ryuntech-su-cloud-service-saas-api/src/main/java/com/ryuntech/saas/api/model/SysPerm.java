package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* <p>
    * 权限
    * </p>
*
* @author antu
* @since 2019-09-12
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysPerm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("perm_id")
    private String permId;

    /**
     * 资源组排序
     */
    @TableField("res_group_sort")
    private Integer resGroupSort;

    /**
     * 资源组名称
     */
    @TableField("res_group_name")
    private String resGroupName;

    /**
     * 同资源组下资源排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 资源名称
     */
    @TableField("res_name")
    private String resName;

    /**
     * 资源标识
     */
    @TableField("res_key")
    private String resKey;

    @TableField(exist = false)
    private List<SysPerm> children = new ArrayList<>();

    @TableField(exist = false)
    Map<Integer, List<SysPerm>> permMap;
    @TableField(exist = false)
    Map<String, List<SysPerm>> btnPermMap;


}
