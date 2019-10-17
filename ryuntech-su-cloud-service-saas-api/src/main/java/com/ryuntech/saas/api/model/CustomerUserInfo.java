package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
* <p>
    * 客户信息表
    * </p>
*
* @author antu
* @since 2019-09-29
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_customer_user_info")
    public class CustomerUserInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

            /**
            * 客户编号
            */
            @TableId("CUSTOMER_ID")
    private String customerId;

            /**
            * 客户姓名
            */
        @TableField("CUSTOMER_NAME")
    private String customerName;

            /**
            * 联系人
            */
        @TableField("CONTACTS")
    private String contacts;

            /**
            * 联系电话
            */
        @TableField("CONTACTS_PHONE")
    private String contactsPhone;

            /**
            * 负责员工编号
            */
        @TableField("STAFF_ID")
    private String staffId;

            /**
            * 负责员工姓名
            */
        @TableField("STAFF_NAME")
    private String staffName;

            /**
            * 部门
            */
        @TableField("DEPARTMENT")
    private String department;

        /**
            * 详细地址
            */
        @TableField("ADDRESS")
    private String address;


    /**
     * 详细地址
     */
    @TableField("CITY")
    private String city;

}
