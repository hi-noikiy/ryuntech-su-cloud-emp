package com.ryuntech.saas.api.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class CustomerRiskDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 风险编号
     */
    private String riskId;

    /**
     * 合同名称
     */
    private String customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 风险时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date riskTime;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date created;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updated;

    /**
     * 风险内容
     */
    private String riskContent;

    /**
     * 风险案号
     */
    private String riskCode;

    /**
     * 风险小类
     */
    private String riskType;

    /**
     * 风险大类
     */
    private String riskMType;
    /**
     * 分类类集合
     */
    private String riskTypes;

    /**
     * 未读已读标记0未读1已读
     */
    private String falg;

}
