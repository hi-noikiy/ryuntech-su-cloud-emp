package com.ryuntech.saas.api.helper.constant;

/**
 * @author EDZ
 */
public class RiskWarnConstants {



//    工商变更	法人变更	高风险	人员相关	个人关联风险-法人变更
//    公司名称变更	提示	工商信息	企业工商数据查询	可能有出入，得和企查查沟通
//    大股东变更	提示	人员相关	个人关联风险-大股东变更

//    法律诉讼	失信被执行人	高风险	法律诉讼	失信被执行人信息
//    股权冻结	高风险	法律诉讼	股权冻结
//    法院公告	警示信息	法律诉讼	查询法院公告
//    立案消息	警示信息	法律诉讼	立案消息
//    开庭公告	警示信息	法律诉讼	查询开庭公告


//    经营风险	司法拍卖	警示信息	经营风险	司法拍卖
//    土地抵押	警示信息	经营风险	土地抵押
//    环保处罚	警示信息	经营风险	环保处罚
//    动产抵押	警示信息	经营风险	动产抵押
//    严重违法	高风险	经营风险	严重违法
//    欠税公告	警示信息	经营风险	欠税公告
//    税收违法	警示信息	经营风险	税收违法
//    行政处罚	警示信息	经营风险	行政处罚
//    企业经营异常	高风险	工商信息	企业经营异常信息


    /*法人变更
            公司名称变更
    大股东变更
            失信被执行人
    股权冻结
            法院公告
    立案消息
            开庭公告
    司法拍卖
            土地抵押
    环保处罚
            动产抵押
    严重违法
            欠税公告
    税收违法
            行政处罚
    企业经营异常*/


//    风险大类

    /**
     * 工商变更
     */
    public static final String INDUSTRIALANDCOMMERCIALCHANGE ="1";

    /**
     * 法律诉讼
     */
    public static final String ACTIONATLAW ="2";

    /**
     * 经营风险
     */
    public static final String OPERATINGRISK ="3";


//    风险小类
    /**
     * 法人变更
     */
    public static final String TYPE1 ="1";
    /**
     * 公司名称变更
     */
    public static final String TYPE2 ="2";
    /**
     * 大股东变更
     */
    public static final String TYPE3 ="3";
    /**
     * 失信被执行人
     */
    public static final String TYPE4 ="4";
    /**
     * 执行人 失信被执行人信息-被执行人信息
     */

    public static final String TYPE5 ="5";
    /**
     * 股权冻结
     */
    public static final String TYPE6 ="6";
    /**
     * 法院公告
     */
    public static final String TYPE7 ="7";
    /**
     * 立案消息
     */
    public static final String TYPE8 ="8";
    /**
     * 开庭公告
     */
    public static final String TYPE9 ="9";
    /**
     * 司法拍卖
     */
    public static final String TYPE10 ="10";
    /**
     * 土地抵押
     */
    public static final String TYPE11 ="11";
    /**
     * 环保处罚
     */
    public static final String TYPE12 ="12";
    /**
     * 动产抵押
     */
    public static final String TYPE13 ="13";
    /**
     * 严重违法
     */
    public static final String TYPE14 ="14";
    /**
     * 欠税公告
     */
    public static final String TYPE15 ="15";
    /**
     * 税收违法
     */
    public static final String TYPE16 ="16";
    /**
     * 行政处罚
     */
    public static final String TYPE17 ="17";
    /**
     * 企业经营异常
     */
    public static final String TYPE18 ="18";
    /**
     * 获取工商快照
     */
    public static final String TYPE19 ="19";

}
