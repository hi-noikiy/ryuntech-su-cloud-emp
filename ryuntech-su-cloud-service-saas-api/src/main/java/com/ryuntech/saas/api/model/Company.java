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
import java.util.Date;

/**
 * <p>
 * 企业表
 * </p>
 *
 * @author antu
 * @since 2019-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ryn_company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("company_id")
    private String companyId;

    /**
     * 公司名称
     */
    @TableField("name")
    private String name;

    /**
     * 公司LOGO
     */
    @TableField("logo")
    private String logo;

    /**
     * 行业类别ID
     */
    @TableField("industry_type")
    private Boolean industryType;

    /**
     * 省ID
     */
    @TableField("PROVINCE_ID")
    private Integer provinceId;

    /**
     * 城市ID
     */
    @TableField("city_id")
    private Integer cityId;

    /**
     * 区ID
     */
    @TableField("district_id")
    private Integer districtId;

    /**
     * 详细地址
     */
    @TableField("address")
    private String address;

    /**
     * 邮编
     */
    @TableField("postal_code")
    private String postalCode;

    /**
     * 电话
     */
    @TableField("tel")
    private String tel;

    /**
     * 传真
     */
    @TableField("fax")
    private String fax;

    /**
     * 联系人
     */
    @TableField("contactor")
    private String contactor;

    /**
     * 负责人员工ID
     */
    @TableField("employee_id")
    private String employeeId;

    /**
     * 职位
     */
    @TableField("position")
    private String position;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * QQ
     */
    @TableField("qq")
    private String qq;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 公司网址
     */
    @TableField("company_web")
    private String companyWeb;

    /**
     * 公司介绍
     */
    @TableField("company_introduce")
    private String companyIntroduce;

    /**
     * 纳税人识别号
     */
    @TableField("taxpayer_identify_num")
    private String taxpayerIdentifyNum;

    /**
     * 发票抬头
     */
    @TableField("invoice_payable")
    private String invoicePayable;

    /**
     * （发票）地址
     */
    @TableField("invoice_address")
    private String invoiceAddress;

    /**
     * （发票）电话
     */
    @TableField("invoice_tel")
    private String invoiceTel;

    /**
     * （发票）开户名称
     */
    @TableField("invoice_open_name")
    private String invoiceOpenName;

    /**
     * （发票）开户银行
     */
    @TableField("invoice_open_bank")
    private String invoiceOpenBank;

    /**
     * （发票）银行账号
     */
    @TableField("invoice_bank_account")
    private String invoiceBankAccount;

    /**
     * 服务热线
     */
    @TableId("hotline_tel")
    private String hotlineTel;

    /**
     * 是否删除：0否 1是
     */
    @TableField("is_del")
    private Boolean isDel;


    /**
     * 企查查是否存在 0 否，1存在
     */
    @TableField("is_qichacha")
    private Boolean isQichacha;

    /**
     * 更新时间
     */
    @TableId("updated_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

    /**
     * 创建时间
     */
    @TableId("created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;


}
