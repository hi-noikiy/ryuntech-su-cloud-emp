package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
    private Date riskTime;

            /**
            * 创建时间
            */
        @TableField("CREATED")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date created;

            /**
            * 修改时间
            */
        @TableField("UPDATED")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updated;

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

        @TableField("KEY_NO")
    private String keyNo;

    /**
     * 风险小类
     */
    @TableField("RISK_TYPE")
    private String riskType;

    /**
     * 风险大类
     */
    @TableField("RISK_MTYPE")
    private String riskMType;

    /**
     * 风险级别
     */
    @TableField("RISK_LEVEL")
    private String riskLevel;


    /**
     * 未读已读标记
     */
    @TableField("FALG")
    private String falg;


}
