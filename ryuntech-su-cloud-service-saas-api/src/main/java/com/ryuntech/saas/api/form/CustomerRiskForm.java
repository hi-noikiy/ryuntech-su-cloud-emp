package com.ryuntech.saas.api.form;


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
public class CustomerRiskForm {

    /**
     * 风险编号
     */
    private String riskId;

    /**
     * 客户编号
     */
    private String customerId;
    private List<String> customerIds;

    /**
     * 客户名称
     */
    private String customerName;


    /**
     * 部门
     */
    private String departmentId;

    /**
     * 查询月份
     */
    private String riskTime;



    /**
     * 风险详情发生时间
     */
    private String riskDetailTime;
    /**
     * 风险类型
     * 风险类型 1 法人变更 2 公司名称变更 3 大股东变更 4 失信被执行人 5 执行人 失信被执行人信息-被执行人信息
     * 6 股权冻结 7 法院公告 9 开庭公告 10 司法拍卖 11 土地抵押 12 环保处罚 13 动产抵押 14 严重违法 15 欠税公告
     * 16 税收违法 17 行政处罚 18 企业经营异常 另工商变更全部：businessAll 法律诉讼全部：legalAll 经营风险全部：manageAll
     */
    private List<String> riskMTypes;
    private String riskMType;
    private String riskType;


    /**
     * 查询月份 0 本月 1上个月 2本周 3上周 4自定义
     */
    private String riskTimeType;

    /**
     * 自定义开始时间
     */
    private String riskStartTime;

    /**
     * 自定义开始时间
     */
    private String riskEndTime;

}
