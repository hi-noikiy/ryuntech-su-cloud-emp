package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 风险监控表
    * </p>
*
* @author antu
* @since 2019-11-20
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_company_monitor")
    public class CompanyMonitor implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 风险编号
            */
            @TableId("MONITOR_ID")
    private String monitorId;

            /**
            * 客户名称
            */
        @TableField("CUSTOMER_ID")
    private String customerId;

            /**
            * 客户名称
            */
        @TableField("CUSTOMER_NAME")
    private String customerName;

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


}
