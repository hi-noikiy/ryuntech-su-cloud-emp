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
    * 客户风险表
    * </p>
*
* @author antu
* @since 2019-11-06
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_customer_risk")
    public class CustomerRisk extends BaseModel {

    private static final long serialVersionUID = 1L;

            /**
            * 风险编号
            */
            @TableId("RISK_ID")
    private String riskId;

            /**
            * 合同名称
            */
        @TableField("CUSTOMER_ID")
    private String customerId;

            /**
            * 客户名称
            */
        @TableField("CUSTOMER_NAME")
    private String customerName;

            /**
            * 风险时间
            */
        @TableField("RISK_TIME")
    private LocalDateTime riskTime;

            /**
            * 创建时间
            */
        @TableField("CREATED")
    private LocalDateTime created;

            /**
            * 修改时间
            */
        @TableField("UPDATED")
    private LocalDateTime updated;

            /**
            * 风险内容
            */
        @TableField("RISK_CONTENT")
    private String riskContent;

            /**
            * 风险案号
            */
        @TableField("RISK_CODE")
    private String riskCode;


}
