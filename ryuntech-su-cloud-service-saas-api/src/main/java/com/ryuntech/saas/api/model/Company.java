package com.ryuntech.saas.api.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ryuntech.common.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
* <p>
    *
    * </p>
*
* @author antu
* @since 2019-10-17
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @TableName("ryn_company")
    public class Company extends BaseModel {

    private static final long serialVersionUID = 1L;



    /**
     * 公司主键
     */
    private String companyId;
            /**
            * 公司名称
            */
    private String name;

            /**
            * 公司logo
            */
    private String logo;

            /**
            * 行业类别ID
            */
    private Boolean industryType;

            /**
            * 省ID
            */
    private Integer provinceId;

            /**
            * 城市ID
            */
    private Integer cityId;

            /**
            * 区ID
            */
    private Integer districtId;

            /**
            * 详细地址
            */
    private String address;

            /**
            * 邮编
            */
    private String postalCode;

            /**
            * 电话
            */
    private String tel;

            /**
            * 传真
            */
    private String fax;

            /**
            * 联系人
            */
    private String contactor;

            /**
            * 负责人员工ID
            */
    private String employeeId;

            /**
            * 职位
            */
    private String position;

            /**
            * 手机号
            */
    private String phone;

            /**
            * QQ
            */
    private String qq;

            /**
            * 邮箱
            */
    private String email;

            /**
            * 公司网址
            */
    private String companyWeb;

            /**
            * 公司介绍
            */
    private String companyIntroduce;

            /**
            * 纳税人识别号
            */
    private String taxpayerIdentifyNum;

            /**
            * 发票抬头
            */
    private String invoicePayable;

            /**
            * （发票）地址
            */
    private String invoiceAddress;

            /**
            * （发票）电话
            */
    private String invoiceTel;

            /**
            * （发票）开户名称
            */
    private String invoiceOpenName;

            /**
            * （发票）开户银行
            */
    private String invoiceOpenBank;

            /**
            * （发票）银行账号
            */
    private String invoiceBankAccount;

            /**
            * 服务热线
            */
    private String hotlineTel;

            /**
            * 是否删除：0否 1是
            */
    private Boolean isDel;

            /**
            * 更新时间
            */
    private Date updatedAt;

            /**
            * 创建时间
            */
    private Date createdAt;


}
