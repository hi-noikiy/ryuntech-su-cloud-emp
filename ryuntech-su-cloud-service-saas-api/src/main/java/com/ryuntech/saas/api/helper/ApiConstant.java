package com.ryuntech.saas.api.helper;

/**
 * @author EDZ
 */
public class ApiConstant {

    public static final String APPKEY = "d84df38ffa9e44b9bdbe94105d106a79";
    public static final String SECKEY = "74D256F4CA4D05758A30FC06BEAD6972";



    //工商变更
    /**
     * 企业工商数据查询
     */
    public static final String SEARCH=" http://api.qichacha.com/ECIV4/Search";

    /**
     * 个人关联风险-大股东变更
     */
    public static final String PRRSTOCKHOLDERCHANGE=" http://api.qichacha.com/PRRStockholderChange/GetList";
    /**
     * 个人关联风险-法人变更
     */
    public static final String PRROPER=" http://api.qichacha.com/PRROper/GetList";


    //法律诉讼
    /**
     * 失信被执行人
     */
    public static final String SEARCHSHIXIN="http://api.qichacha.com/CourtV4/SearchShiXin";
    /**
     * 被执行人信息
     */
    public static final String SEARCHZHIXING="http://api.qichacha.com/CourtV4/SearchZhiXing";
    /**
     * 股权冻结
     */
    public static final String GETJUDICIALASSISTANCE="http://api.qichacha.com/JudicialAssistance/GetJudicialAssistance";
    /**
     * 查询法院公告
     */
    public static final String SEARCHCOURTANNOUNCEMENT="http://api.qichacha.com/CourtNoticeV4/SearchCourtAnnouncement";
    /**
     * 立案信息
     */
    public static final String CASEFILING="http://api.qichacha.com/CaseFiling/GetList";
    /**
     * 查询开庭公告
     */
    public static final String SEARCHCOURTNOTICE="http://api.qichacha.com/CourtAnnoV4/SearchCourtNotice";




    //经营风险
    /**
     * 司法拍卖
     */
    public static final String GETJUDICIALSALELIST="http://api.qichacha.com/JudicialSale/GetJudicialSaleList";
    /**
     * 土地抵押接口
     */
    public static final String GETLANDMORTGAGELIST="http://api.qichacha.com/LandMortgage/GetLandMortgageList";

    /**
     * 环保处罚接口
     */
    public static final String GETENVPUNISHMENTLIST="http://api.qichacha.com/EnvPunishment/GetEnvPunishmentList";
    /**
     * 动产抵押查询
     */
    public static final String GETCHATTELMORTGAGE="http://api.qichacha.com/ChattelMortgage/GetChattelMortgage";
    /**
     * 严重违法
     */
    public static final String GETSERIOUSVIOLATIONLIST="http://api.qichacha.com/SeriousViolation/GetSeriousViolationList";

    /**
     * 欠税公告
     */
    public static final String GETTAXOWENOTICELIST="http://api.qichacha.com/TaxOweNotice/GetTaxOweNoticeList";

    /**
     * 税收违法
     */
    public static final String TAXILLEGALLIST="http://api.qichacha.com/TaxIllegal/TaxIllegalList";

    /**
     * 行政处罚
     */
    public static final String GETGSADMINISTRATIVEPENALTYLIST="http://api.qichacha.com/AdministrativePenalty/GetGsAdministrativePenaltyList";

    /**
     * 企业经营异常信息
     */
    public static final String GETOPEXCEPTION="http://api.qichacha.com/ECIException/GetOpException";

    /**
     * 获取工商快照
     */
    public static final String GETECIIMAGE="http://api.qichacha.com/ECIImage/GetEciImage";

}
