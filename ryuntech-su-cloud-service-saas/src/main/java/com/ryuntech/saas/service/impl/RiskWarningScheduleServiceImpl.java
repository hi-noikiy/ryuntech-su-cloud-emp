package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.ryuntech.common.constant.generator.IncrementIdGenerator;
import com.ryuntech.common.constant.generator.UniqueIdGenerator;
import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.common.utils.HttpUtils;
import com.ryuntech.saas.api.helper.ApiConstant;
import com.ryuntech.saas.api.helper.constant.RiskWarnConstants;
import com.ryuntech.saas.api.mapper.CustomerMonitorMapper;
import com.ryuntech.saas.api.mapper.CustomerRiskMapper;
import com.ryuntech.saas.api.mapper.CustomerUserInfoMapper;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.RiskWarningScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.ryuntech.saas.api.helper.ApiConstant.SEARCHCOURTNOTICE;
import static com.ryuntech.saas.api.helper.ApiConstant.APPKEY;
import static com.ryuntech.saas.api.helper.ApiConstant.SEARCH;
import static com.ryuntech.saas.api.helper.ApiConstant.PRRSTOCKHOLDERCHANGE;
import static com.ryuntech.saas.api.helper.ApiConstant.SEARCHSHIXIN;
import static com.ryuntech.saas.api.helper.ApiConstant.SEARCHZHIXING;
import static com.ryuntech.saas.api.helper.ApiConstant.GETJUDICIALASSISTANCE;
import static com.ryuntech.saas.api.helper.ApiConstant.SEARCHCOURTANNOUNCEMENT;
import static com.ryuntech.saas.api.helper.ApiConstant.CASEFILING;
import static com.ryuntech.saas.api.helper.ApiConstant.PRROPER;

import static com.ryuntech.saas.api.helper.ApiConstant.GETJUDICIALSALELIST;
import static com.ryuntech.saas.api.helper.ApiConstant.GETLANDMORTGAGELIST;
import static com.ryuntech.saas.api.helper.ApiConstant.GETENVPUNISHMENTLIST;
import static com.ryuntech.saas.api.helper.ApiConstant.GETCHATTELMORTGAGE;
import static com.ryuntech.saas.api.helper.ApiConstant.GETSERIOUSVIOLATIONLIST;
import static com.ryuntech.saas.api.helper.ApiConstant.GETTAXOWENOTICELIST;
import static com.ryuntech.saas.api.helper.ApiConstant.TAXILLEGALLIST;
import static com.ryuntech.saas.api.helper.ApiConstant.GETGSADMINISTRATIVEPENALTYLIST;
import static com.ryuntech.saas.api.helper.ApiConstant.GETOPEXCEPTION;

/**
 * @author EDZ
 */

/**
 * 定时器，检测企业动态变化
 * @Scheduled(cron = "0 0 2 * * ?")
 * @author EDZ
 */
@Slf4j
@Component
public class RiskWarningScheduleServiceImpl implements RiskWarningScheduleService {

    @Autowired
    private CustomerUserInfoMapper customerUserInfoMapper;

    @Autowired
    private CustomerRiskMapper customerRiskMapper;

    @Autowired
    private CustomerMonitorMapper customerMonitorMapper;

    private List<String> urlList=new ArrayList<>(Arrays.asList(
            /**
             * 工商变更
             */
//            法人变更
            PRROPER+"?key="+APPKEY,
//            司名称变更
//            个人关联风险-大股东变更
            PRRSTOCKHOLDERCHANGE+"?key="+APPKEY,
            /**
             * 法律诉讼
             */
            SEARCHSHIXIN+"?key="+APPKEY,
            SEARCHZHIXING+"?key="+APPKEY,
            GETJUDICIALASSISTANCE+"?key="+APPKEY,
            SEARCHCOURTANNOUNCEMENT+"?key="+APPKEY,
            CASEFILING+"?key="+APPKEY,
            SEARCHCOURTNOTICE+"?key="+APPKEY,
            /**
             * 经营风险
             */
            GETJUDICIALSALELIST+"?key="+APPKEY,
            GETLANDMORTGAGELIST+"?key="+APPKEY,
            GETENVPUNISHMENTLIST+"?key="+APPKEY,
            GETCHATTELMORTGAGE+"?key="+APPKEY,
            GETSERIOUSVIOLATIONLIST+"?key="+APPKEY,
            GETTAXOWENOTICELIST+"?key="+APPKEY,
            TAXILLEGALLIST+"?key="+APPKEY,
            GETGSADMINISTRATIVEPENALTYLIST+"?key="+APPKEY,
            GETOPEXCEPTION+"?key="+APPKEY

    ));



    /**
     * 每天凌晨2点执行
     */
    @Override
    @Scheduled(cron = "0 05 03 ? * *")
    public void riskWarning() {
        /**
         * 轮询客户风险监控列表
         */
        log.info("riskWarning=");
//        查询监控数据
        List<CustomerMonitor> customerMonitors = customerMonitorMapper.selectList(new QueryWrapper<>());
        if (customerMonitors.isEmpty()){
            log.info("没有待监控的企业");
            return;
        }
        log.info("开始监控");
        List<String> stringList = new ArrayList<>();
        for (CustomerMonitor customerMonitor :customerMonitors){
            stringList.add(customerMonitor.getCustomerId());
        }
        List<CustomerUserInfo> customerUserInfoList = customerUserInfoMapper.selectBatchIds(stringList);
        for (CustomerUserInfo customerUserInfo :customerUserInfoList){
            String customerName = customerUserInfo.getCustomerName();
            for (String urlName :urlList){
                try {
                    String[] autherHeader = randomAuthentHeader();
                    HashMap<String, String> reqHeader = new HashMap<>();
                    reqHeader.put("Token",autherHeader[0]);
                    reqHeader.put("Timespan",autherHeader[1]);
                    Gson gson = new Gson();
                    if (urlName.contains(PRROPER)) {
//                        法人变更
                        urlName+="&searchKey="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiPrroper riskWarning = gson.fromJson(content, ApiPrroper.class);
                        if (riskWarning != null && riskWarning.getResults().size() != 0) {
//                    插入风险预警
                            if (null != riskWarning.getResults() && riskWarning.getResults().size() != 0) {
                                for(ApiPrroper.Result result:riskWarning.getResults()){
                                    CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                    customerRisk.setRiskTime(DateUtil.parseDateTime(result.getOperList().get(0).getChangeDate()));
                                    customerRisk.setRiskContent(content);
//                                风险大类
                                    customerRisk.setRiskType(RiskWarnConstants.INDUSTRIALANDCOMMERCIALCHANGE);
//                                风险小类
                                    customerRisk.setRiskMType(RiskWarnConstants.TYPE1);
                                    customerRiskMapper.insert(customerRisk);
                                }
                            }
                        }
                    } else if (urlName.contains(PRRSTOCKHOLDERCHANGE)) {
//                        大股东变更
                        urlName+="&searchKey="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiPrrStockholderChange apiPrrStockholderChange = gson.fromJson(content, ApiPrrStockholderChange.class);
                        if (apiPrrStockholderChange != null && apiPrrStockholderChange.getResults().size() != 0) {
                            for(ApiPrrStockholderChange.Result result:apiPrrStockholderChange.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getPartnerList().get(0).getChangeDate()));
                                customerRisk.setRiskContent(content);
    //                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.INDUSTRIALANDCOMMERCIALCHANGE);
    //                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE3);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(SEARCH)) {

                    } else if (urlName.contains(SEARCHSHIXIN)) {
//                           失信被执行人
                        urlName+="&searchKey="+customerName+"&isExactlySame=true";
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiSearchShiXin apiSearchShiXin = gson.fromJson(content, ApiSearchShiXin.class);
                        if (apiSearchShiXin != null && apiSearchShiXin.getResults().size() != 0) {
                            for(ApiSearchShiXin.Result result:apiSearchShiXin.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getUpdatedate()));
                                customerRisk.setRiskContent(content);
//                                 风险案号
                                customerRisk.setRiskCode(result.getOrgno());
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.ACTIONATLAW);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE4);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(SEARCHZHIXING)) {
//                      被执行人信息
                        urlName+="&searchKey="+customerName+"&isExactlySame=true";
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiSearchZhiXing apiSearchShiXin = gson.fromJson(content, ApiSearchZhiXing.class);
                        if (apiSearchShiXin != null && apiSearchShiXin.getResults().size() != 0) {
                            for(ApiSearchZhiXing.Result result:apiSearchShiXin.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getUpdatedate()));
                                customerRisk.setRiskCode(result.getAnno());
                                customerRisk.setRiskContent(content);
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.ACTIONATLAW);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE5);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETJUDICIALASSISTANCE)){
//                        股权冻结
                        urlName+="&searchKey="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiGetJudicialAssistance apiGetJudicialAssistance = gson.fromJson(content, ApiGetJudicialAssistance.class);
                        if (apiGetJudicialAssistance != null && apiGetJudicialAssistance.getResults().size() != 0) {
                            for(ApiGetJudicialAssistance.Result result:apiGetJudicialAssistance.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getEquityFreezeDetail().getFreezeStartDate()));
                                customerRisk.setRiskCode(result.getEquityFreezeDetail().getExecutionVerdictNum());
                                customerRisk.setRiskContent(content);
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.ACTIONATLAW);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE6);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(SEARCHCOURTANNOUNCEMENT)){
//                        法院公告
                        urlName+="&keyWord="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiSearchCourtAnnouncement apiSearchCourtAnnouncement = gson.fromJson(content, ApiSearchCourtAnnouncement.class);
                        if (apiSearchCourtAnnouncement != null && apiSearchCourtAnnouncement.getResults().size() != 0) {
                            for(ApiSearchCourtAnnouncement.Result result:apiSearchCourtAnnouncement.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getPublishedDate()));
                                customerRisk.setRiskContent(content);
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.ACTIONATLAW);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE7);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(CASEFILING)){
//                      立案信息
                        urlName+="&searchKey="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiCaseFiling apiCaseFiling = gson.fromJson(content, ApiCaseFiling.class);
                        if (apiCaseFiling != null && apiCaseFiling.getResults().size() != 0) {
                            for(ApiCaseFiling.Result result:apiCaseFiling.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getPublishDate()));
                                customerRisk.setRiskContent(content);
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.ACTIONATLAW);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE8);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(SEARCHCOURTNOTICE)){
//                        开庭公告
                        urlName+="&searchKey="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiSearchCourtNotice apiSearchCourtNotice = gson.fromJson(content, ApiSearchCourtNotice.class);
                        if (apiSearchCourtNotice != null && apiSearchCourtNotice.getResults().size() != 0) {
                            for(ApiSearchCourtNotice.Result result:apiSearchCourtNotice.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getLianDate()));
                                customerRisk.setRiskContent(content);
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.ACTIONATLAW);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE9);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETJUDICIALSALELIST)){
//                      司法拍卖
                        urlName+="&keyWord="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiGetJudicialSaleList apiGetJudicialSaleList = gson.fromJson(content, ApiGetJudicialSaleList.class);
                        if (apiGetJudicialSaleList != null && apiGetJudicialSaleList.getResults().size() != 0) {
                            for(ApiGetJudicialSaleList.Result result:apiGetJudicialSaleList.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(content);
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE10);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETLANDMORTGAGELIST)){
//                      土地抵押接口
                        urlName+="&keyWord="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiGetLandMortgageList apiGetLandMortgageList = gson.fromJson(content, ApiGetLandMortgageList.class);
                        if (apiGetLandMortgageList != null && apiGetLandMortgageList.getResults().size() != 0) {
                            for(ApiGetLandMortgageList.Result result:apiGetLandMortgageList.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(content);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getStartDate()));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE11);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETENVPUNISHMENTLIST)){
//                        环保处罚接口
                        urlName+="&keyWord="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiGetEnvPunishmentList apiGetEnvPunishmentList = gson.fromJson(content, ApiGetEnvPunishmentList.class);
                        if (apiGetEnvPunishmentList != null && apiGetEnvPunishmentList.getResults().size() != 0) {
                            for(ApiGetEnvPunishmentList.Result result:apiGetEnvPunishmentList.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(content);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getPunishDate()));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE12);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETCHATTELMORTGAGE)){
//                        动产抵押查询
                        urlName+="&keyWord="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiGetChattelMortgage apiGetChattelMortgage = gson.fromJson(content, ApiGetChattelMortgage.class);
                        if (apiGetChattelMortgage != null && apiGetChattelMortgage.getResults().size() != 0) {
                            for(ApiGetChattelMortgage.Result result:apiGetChattelMortgage.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(content);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getPublicDate()));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE13);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETSERIOUSVIOLATIONLIST)){
//                        严重违法
                        urlName+="&keyWord="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiGetSeriousViolationList apiGetSeriousViolationList = gson.fromJson(content, ApiGetSeriousViolationList.class);
                        if (apiGetSeriousViolationList != null && apiGetSeriousViolationList.getResults().size() != 0) {
                            for(ApiGetSeriousViolationList.Result result:apiGetSeriousViolationList.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(content);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getAddDate()));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE14);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETTAXOWENOTICELIST)){
//                        欠税公告
                        urlName+="&keyWord="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiGetTaxOweNoticeList apiGetTaxOweNoticeList = gson.fromJson(content, ApiGetTaxOweNoticeList.class);

                        if (apiGetTaxOweNoticeList != null && apiGetTaxOweNoticeList.getResults().size() != 0) {
                            for(ApiGetTaxOweNoticeList.Result result:apiGetTaxOweNoticeList.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(content);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getPublishDate()));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE15);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(TAXILLEGALLIST)){
//                        欠税公告
                        urlName+="&searchKey="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiTaxIllegalList apiTaxIllegalList = gson.fromJson(content, ApiTaxIllegalList.class);
                        if (apiTaxIllegalList != null && apiTaxIllegalList.getResults().size() != 0) {
                            for(ApiTaxIllegalList.Result result:apiTaxIllegalList.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(content);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getIllegalTime()));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE16);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETGSADMINISTRATIVEPENALTYLIST)){
//                        行政处罚
                        urlName+="&searchKey="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiGetGsAdministrativePenaltyList apiGetGsAdministrativePenaltyList =
                                gson.fromJson(content, ApiGetGsAdministrativePenaltyList.class);
                        if (apiGetGsAdministrativePenaltyList != null && apiGetGsAdministrativePenaltyList.getResults().size() != 0) {
                            for(ApiGetGsAdministrativePenaltyList.Result result:apiGetGsAdministrativePenaltyList.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(content);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getPublicDate()));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE17);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETOPEXCEPTION)){
//                      企业经营异常信息
                        urlName+="&keyNo="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        ApiGetOpException apiGetOpException =
                                gson.fromJson(content, ApiGetOpException.class);
                        if (apiGetOpException != null && apiGetOpException.getResults().size() != 0) {
                            for(ApiGetOpException.Result result:apiGetOpException.getResults()) {
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(content);
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getAddDate()));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE18);
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }
                    log.info("结束监控");
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("监控异常"+e.toString());
                }
            }
        }
    }

    protected CustomerRisk setCustomerRiskDate(CustomerUserInfo customerUserInfo){
        CustomerRisk customerRisk = new CustomerRisk();
        customerRisk.setCreated(new Date());
        customerRisk.setUpdated(new Date());
        customerRisk.setCustomerId(customerUserInfo.getCustomerId());
        customerRisk.setCustomerName(customerUserInfo.getCustomerName());
        Long aLong = UniqueIdGenerator.getInstance(IncrementIdGenerator.getServiceId()).nextId();
        customerRisk.setRiskId(String.valueOf(aLong));
        return customerRisk;
    }

    /**
     * 获取Auth Code
      * @return
     */
    protected static final String[] randomAuthentHeader() {
        String timeSpan = String.valueOf(System.currentTimeMillis() / 1000);
        return new String[] { DigestUtils.md5Hex(APPKEY.concat(timeSpan).concat(ApiConstant.SECKEY)).toUpperCase(), timeSpan };
    }
}
