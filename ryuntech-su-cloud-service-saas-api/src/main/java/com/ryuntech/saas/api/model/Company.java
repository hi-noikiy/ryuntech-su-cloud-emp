package com.ryuntech.saas.api.model;

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
    @TableId("COMPANY_ID")
    private String companyId;
            /**
            * 公司名称
            */
            @TableId("NAME")
    private String name;

            /**
            * 公司logo
            */
            @TableId("LOGO")
    private String logo;

            /**
            * 行业类别ID
            */
            @TableId("INDUSTRY_TYPE")
    private Boolean industryType;

            /**
            * 省ID
            */
            @TableId("PROVINCE_ID")
    private Integer provinceId;

            /**
            * 城市ID
            */
            @TableId("CITY_ID")
    private Integer cityId;

            /**
            * 区ID
            */
            @TableId("DISTRICT_ID")
    private Integer districtId;

            /**
            * 详细地址
            */
            @TableId("ADDRESS")
    private String address;

            /**
            * 邮编
            */
            @TableId("POSTAL_CODE")
    private String postalCode;

            /**
            * 电话
            */
            @TableId("TEL")
    private String tel;

            /**
            * 传真
            */
            @TableId("FAX")
    private String fax;

            /**
            * 联系人
            */
            @TableId("CONTACTOR")
    private String contactor;

            /**
            * 负责人员工ID
            */
            @TableId("EMPLOYEE_ID")
    private String employeeId;

            /**
            * 职位
            */
            @TableId("POSITION")
    private String position;

            /**
            * 手机号
            */
            @TableId("PHONE")
    private String phone;

            /**
            * QQ
            */
            @TableId("QQ")
    private String qq;

            /**
            * 邮箱
            */
            @TableId("EMAIL")
    private String email;

            /**
            * 公司网址
            */
            @TableId("COMPANY_WEB")
    private String companyWeb;

            /**
            * 公司介绍
            */
            @TableId("COMPANY_INTRODUCE")
    private String companyIntroduce;

            /**
            * 纳税人识别号
            */
            @TableId("TAXPAYER_IDENTIFY_NUM")
    private String taxpayerIdentifyNum;

            /**
            * 发票抬头
            */
            @TableId("INVOICE_PAYABLE")
    private String invoicePayable;

            /**
            * （发票）地址
            */
            @TableId("INVOICE_ADDRESS")
    private String invoiceAddress;

            /**
            * （发票）电话
            */
            @TableId("INVOICE_TEL")
    private String invoiceTel;

            /**
            * （发票）开户名称
            */
            @TableId("INVOICE_OPEN_NAME")
    private String invoiceOpenName;

            /**
            * （发票）开户银行
            */
            @TableId("INVOICE_OPEN_BANK")
    private String invoiceOpenBank;

            /**
            * （发票）银行账号
            */
            @TableId("INVOICE_BANK_ACCOUNT")
    private String invoiceBankAccount;

            /**
            * 服务热线
            */
            @TableId("HOTLINE_TEL")
    private String hotlineTel;

            /**
            * 是否删除：0否 1是
            */
            @TableId("IS_DEL")
    private Boolean isDel;

            /**
            * 更新时间
            */
            @TableId("UPDATED_AT")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

            /**
            * 创建时间
            */
            @TableId("CREATED_AT")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;


}
