package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
* <p>
    * 客户风险监控表
    * </p>
*
* @author antu
* @since 2019-11-20
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_customer_monitor")
    public class CustomerMonitor implements Serializable {

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


        @TableField("STAFF_ID")
    private String staffId;


        @TableField("STAFF_NAME")
    private String staffName;

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


}
