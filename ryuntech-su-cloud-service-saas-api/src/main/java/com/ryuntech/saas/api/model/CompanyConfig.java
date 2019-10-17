package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 公司系统设置表
    * </p>
*
* @author antu
* @since 2019-10-17
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_company_config")
    public class CompanyConfig extends BaseModel {

    private static final long serialVersionUID = 1L;

            /**
            * 所属公司ID
            */
    private String companyId;

            /**
            * 配置名称
            */
    private String name;

            /**
            * 配置值
            */
    private String value;

            /**
            * 更新时间
            */
    private LocalDateTime updatedAt;

            /**
            * 创建时间
            */
    private LocalDateTime createdAt;


}
