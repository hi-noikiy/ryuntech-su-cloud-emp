package com.ryuntech.saas.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.ryuntech.common.constant.generator.IncrementIdGenerator;
import com.ryuntech.common.constant.generator.UniqueIdGenerator;
import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.common.utils.HttpUtils;
import com.ryuntech.saas.api.helper.constant.ApiConstants;
import com.ryuntech.saas.api.helper.constant.RiskWarnConstants;
import com.ryuntech.saas.api.mapper.CompanyMapper;
import com.ryuntech.saas.api.mapper.CustomerMonitorMapper;
import com.ryuntech.saas.api.mapper.CustomerRiskMapper;
import com.ryuntech.saas.api.mapper.CustomerUserInfoMapper;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.RiskWarningScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.ryuntech.saas.api.helper.constant.ApiConstants.SEARCHCOURTNOTICE;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.APPKEY;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.SEARCH;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.PRRSTOCKHOLDERCHANGE;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.SEARCHSHIXIN;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.SEARCHZHIXING;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.GETJUDICIALASSISTANCE;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.SEARCHCOURTANNOUNCEMENT;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.CASEFILING;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.PRROPER;

import static com.ryuntech.saas.api.helper.constant.ApiConstants.GETJUDICIALSALELIST;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.GETLANDMORTGAGELIST;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.GETENVPUNISHMENTLIST;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.GETCHATTELMORTGAGE;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.GETSERIOUSVIOLATIONLIST;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.GETTAXOWENOTICELIST;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.TAXILLEGALLIST;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.GETGSADMINISTRATIVEPENALTYLIST;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.GETOPEXCEPTION;

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


    @Autowired
    private CompanyMapper companyMapper;

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
    @Scheduled(cron = "0 0 2 * * ?")
    public void riskWarning() {
        /**
         * 轮询客户风险监控列表
         */
        log.info("riskWarning=");
//        查询监控数据
        List<CustomerMonitor> customerMonitors = customerMonitorMapper.selectList(new QueryWrapper<CustomerMonitor>().eq("status",true));
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
            Company company = companyMapper.selectById(customerUserInfo.getCompanyId());
            company.setOperName(company.getOperName()==null?"":company.getOperName());
            for (String urlName :urlList){
                try {
                    String[] autherHeader = randomAuthentHeader();
                    HashMap<String, String> reqHeader = new HashMap<>();
                    reqHeader.put("Token",autherHeader[0]);
                    reqHeader.put("Timespan",autherHeader[1]);
                    Gson gson = new Gson();
                    if (urlName.contains(PRROPER)) {
                        log.info("开始法人变更");
//                        法人变更
//                        查询公司
                        urlName=urlName+"&searchKey="+customerName+"&personName="+company.getOperName();
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiPrroper riskWarning = gson.fromJson(content, ApiPrroper.class);
                        if (riskWarning != null &&null!=riskWarning.getResults()&& !riskWarning.getResults().isEmpty()) {
//                    插入风险预警
                            if (null != riskWarning.getResults() && riskWarning.getResults().size() != 0) {
                                for(ApiPrroper.Result result:riskWarning.getResults()){
                                    String keyNo = result.getKeyNo();
                                    CustomerRisk key_no = customerRiskMapper.selectOne(new QueryWrapper<CustomerRisk>().eq("key_no", keyNo));
                                    if (null!=key_no){
                                        continue;
                                    }
                                    CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                    customerRisk.setRiskTime(new Date());
                                    customerRisk.setRiskContent(new Gson().toJson(result));
                                    customerRisk.setKeyNo(keyNo);
//                                风险大类
                                    customerRisk.setRiskType(RiskWarnConstants.INDUSTRIALANDCOMMERCIALCHANGE);
//                                风险小类
                                    customerRisk.setRiskMType(RiskWarnConstants.TYPE1);
                                    //                                风险级别
                                    customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_1);
                                    if (!result.getOperList().isEmpty()&&null!=result.getOperList().get(0)){
                                        customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getOperList().get(0).getChangeDate()));
                                    }
//                                    标记
                                    customerRisk.setFalg("0");
                                    customerRiskMapper.insert(customerRisk);
                                }
                            }
                        }
                    } else if (urlName.contains(PRRSTOCKHOLDERCHANGE)) {
//                        大股东变更
                        log.info("开始大股东变更");
                        urlName=urlName+"&searchKey="+customerName+"&personName="+company.getOperName();
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiPrrStockholderChange apiPrrStockholderChange = gson.fromJson(content, ApiPrrStockholderChange.class);
                        if (apiPrrStockholderChange != null&&null!=apiPrrStockholderChange.getResults() && apiPrrStockholderChange.getResults().isEmpty()) {
                            for(ApiPrrStockholderChange.Result result:apiPrrStockholderChange.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskTime(new Date());
                                customerRisk.setRiskContent(new Gson().toJson(result));
    //                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.INDUSTRIALANDCOMMERCIALCHANGE);
                                //                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_2);
    //                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE3);

                                if (!result.getPartnerList().isEmpty()&&null!=result.getPartnerList().get(0)){
                                    customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getPartnerList().get(0).getChangeDate()));
                                }
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(SEARCH)) {

                    } else if (urlName.contains(SEARCHSHIXIN)) {
//                           失信被执行人
                        log.info("开始失信被执行人");
                        urlName=urlName+"&searchKey="+customerName+"&isExactlySame=true";
//                        String content = HttpUtils.Get(urlName,reqHeader);
                        String content = "{\n" +
                                "\t\"OrderNumber\": \"COURT2019121111191889018563\",\n" +
                                "\t\"Paging\": {\n" +
                                "\t\t\"PageSize\": 10,\n" +
                                "\t\t\"PageIndex\": 1,\n" +
                                "\t\t\"TotalRecords\": 30\n" +
                                "\t},\n" +
                                "\t\"Result\": [{\n" +
                                "\t\t\"Id\": \"f12729fa8795870b914af3e1b47d464c2\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Uniqueno\": \"\",\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-10-30 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京01执1014号\",\n" +
                                "\t\t\"Orgno\": \"58255643-8\",\n" +
                                "\t\t\"Ownername\": \"\",\n" +
                                "\t\t\"Executegov\": \"北京市第一中级人民法院\",\n" +
                                "\t\t\"Province\": \"北京\",\n" +
                                "\t\t\"Executeunite\": \"北京知识产权法院\",\n" +
                                "\t\t\"Yiwu\": \"应偿还申请人人民币153,000.00 元\",\n" +
                                "\t\t\"Executestatus\": \"全部未履行\",\n" +
                                "\t\t\"Actionremark\": \"违反财产报告制度\",\n" +
                                "\t\t\"Publicdate\": \"2019-11-18 00:00:00\",\n" +
                                "\t\t\"Age\": 0,\n" +
                                "\t\t\"Sexy\": \"\",\n" +
                                "\t\t\"Updatedate\": \"\",\n" +
                                "\t\t\"Executeno\": \"(2017)京73民初551号\",\n" +
                                "\t\t\"Performedpart\": \"暂无\",\n" +
                                "\t\t\"Unperformpart\": \"暂无\",\n" +
                                "\t\t\"OrgType\": \"2\",\n" +
                                "\t\t\"OrgTypeName\": \"失信企业\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"f1c1f37d9a98c17fc8c6cc7e645f99f62\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Uniqueno\": \"\",\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-06-14 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京03执781号\",\n" +
                                "\t\t\"Orgno\": \"58255643-8\",\n" +
                                "\t\t\"Ownername\": \"\",\n" +
                                "\t\t\"Executegov\": \"北京市第三中级人民法院\",\n" +
                                "\t\t\"Province\": \"北京\",\n" +
                                "\t\t\"Executeunite\": \"中国国际经济贸易仲裁委员会\",\n" +
                                "\t\t\"Yiwu\": \"支付十亿元及相应投资收益、违约金\",\n" +
                                "\t\t\"Executestatus\": \"全部未履行\",\n" +
                                "\t\t\"Actionremark\": \"违反财产报告制度\",\n" +
                                "\t\t\"Publicdate\": \"2019-12-09 00:00:00\",\n" +
                                "\t\t\"Age\": 0,\n" +
                                "\t\t\"Sexy\": \"\",\n" +
                                "\t\t\"Updatedate\": \"\",\n" +
                                "\t\t\"Executeno\": \"2019中国贸仲京裁字第0394号\",\n" +
                                "\t\t\"Performedpart\": \"暂无\",\n" +
                                "\t\t\"Unperformpart\": \"暂无\",\n" +
                                "\t\t\"OrgType\": \"2\",\n" +
                                "\t\t\"OrgTypeName\": \"失信企业\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"4d9f89f7ccb250cecb862a510eefea492\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Uniqueno\": \"\",\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-06-03 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京03执722号\",\n" +
                                "\t\t\"Orgno\": \"58255643-8\",\n" +
                                "\t\t\"Ownername\": \"\",\n" +
                                "\t\t\"Executegov\": \"北京市第三中级人民法院\",\n" +
                                "\t\t\"Province\": \"北京\",\n" +
                                "\t\t\"Executeunite\": \"北京市第三中级人民法院\",\n" +
                                "\t\t\"Yiwu\": \"金钱给付\",\n" +
                                "\t\t\"Executestatus\": \"全部未履行\",\n" +
                                "\t\t\"Actionremark\": \"违反财产报告制度\",\n" +
                                "\t\t\"Publicdate\": \"2019-08-26 00:00:00\",\n" +
                                "\t\t\"Age\": 0,\n" +
                                "\t\t\"Sexy\": \"\",\n" +
                                "\t\t\"Updatedate\": \"\",\n" +
                                "\t\t\"Executeno\": \"(2017)京03民初231号\",\n" +
                                "\t\t\"Performedpart\": \"暂无\",\n" +
                                "\t\t\"Unperformpart\": \"暂无\",\n" +
                                "\t\t\"OrgType\": \"2\",\n" +
                                "\t\t\"OrgTypeName\": \"失信企业\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"c3077761310ad791536c339019cb057e2\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Uniqueno\": \"\",\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-04-26 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京03执531号\",\n" +
                                "\t\t\"Orgno\": \"58255643-8\",\n" +
                                "\t\t\"Ownername\": \"\",\n" +
                                "\t\t\"Executegov\": \"北京市第三中级人民法院\",\n" +
                                "\t\t\"Province\": \"北京\",\n" +
                                "\t\t\"Executeunite\": \"北京市第三中级人民法院\",\n" +
                                "\t\t\"Yiwu\": \"三被执行人支付人民币八千二百零八万六千六百五十七元及利息。\",\n" +
                                "\t\t\"Executestatus\": \"全部未履行\",\n" +
                                "\t\t\"Actionremark\": \"违反财产报告制度\",\n" +
                                "\t\t\"Publicdate\": \"2019-06-25 00:00:00\",\n" +
                                "\t\t\"Age\": 0,\n" +
                                "\t\t\"Sexy\": \"\",\n" +
                                "\t\t\"Updatedate\": \"\",\n" +
                                "\t\t\"Executeno\": \"(2017)京03民初204号\",\n" +
                                "\t\t\"Performedpart\": \"暂无\",\n" +
                                "\t\t\"Unperformpart\": \"暂无\",\n" +
                                "\t\t\"OrgType\": \"2\",\n" +
                                "\t\t\"OrgTypeName\": \"失信企业\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"d92f1ac0b4e1cf3f84093984069615832\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Uniqueno\": \"\",\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-04-24 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京03执518号\",\n" +
                                "\t\t\"Orgno\": \"58255643-8\",\n" +
                                "\t\t\"Ownername\": \"\",\n" +
                                "\t\t\"Executegov\": \"北京市第三中级人民法院\",\n" +
                                "\t\t\"Province\": \"北京\",\n" +
                                "\t\t\"Executeunite\": \"北京市第三中级人民法院\",\n" +
                                "\t\t\"Yiwu\": \"向申请执行人支付人民币1500万元。\",\n" +
                                "\t\t\"Executestatus\": \"全部未履行\",\n" +
                                "\t\t\"Actionremark\": \"违反财产报告制度\",\n" +
                                "\t\t\"Publicdate\": \"2019-05-28 00:00:00\",\n" +
                                "\t\t\"Age\": 0,\n" +
                                "\t\t\"Sexy\": \"\",\n" +
                                "\t\t\"Updatedate\": \"\",\n" +
                                "\t\t\"Executeno\": \"(2019)京03民初25号\",\n" +
                                "\t\t\"Performedpart\": \"暂无\",\n" +
                                "\t\t\"Unperformpart\": \"暂无\",\n" +
                                "\t\t\"OrgType\": \"2\",\n" +
                                "\t\t\"OrgTypeName\": \"失信企业\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"0324b3f4d7d51fa6e9d006c347d601ad2\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Uniqueno\": \"\",\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-04-09 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京01执364号\",\n" +
                                "\t\t\"Orgno\": \"58255643-8\",\n" +
                                "\t\t\"Ownername\": \"\",\n" +
                                "\t\t\"Executegov\": \"北京市第一中级人民法院\",\n" +
                                "\t\t\"Province\": \"北京\",\n" +
                                "\t\t\"Executeunite\": \"北京知识产权法院\",\n" +
                                "\t\t\"Yiwu\": \"一、乐视控股（北京）有限公司于本判决生效之日起十日内向北京汇思诚业知识产权代理有限公司支付专利代理费、代缴官方费用共计700 936元；二、乐视控股（北京）有限公司于本判决生效之日起十日内向北京汇思诚业知识产权代理有限公司支付利息损失1401.85元；三、驳回北京汇思诚业知识产权代理有限公司的其他诉讼请求。...\",\n" +
                                "\t\t\"Executestatus\": \"全部未履行\",\n" +
                                "\t\t\"Actionremark\": \"违反财产报告制度\",\n" +
                                "\t\t\"Publicdate\": \"2019-06-11 00:00:00\",\n" +
                                "\t\t\"Age\": 0,\n" +
                                "\t\t\"Sexy\": \"\",\n" +
                                "\t\t\"Updatedate\": \"\",\n" +
                                "\t\t\"Executeno\": \"(2017)京73民初1262号\",\n" +
                                "\t\t\"Performedpart\": \"暂无\",\n" +
                                "\t\t\"Unperformpart\": \"暂无\",\n" +
                                "\t\t\"OrgType\": \"2\",\n" +
                                "\t\t\"OrgTypeName\": \"失信企业\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"2b5799b6c317180111331f88af30bc722\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Uniqueno\": \"\",\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-03-01 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京03执301号\",\n" +
                                "\t\t\"Orgno\": \"58255643-8\",\n" +
                                "\t\t\"Ownername\": \"\",\n" +
                                "\t\t\"Executegov\": \"北京市第三中级人民法院\",\n" +
                                "\t\t\"Province\": \"北京\",\n" +
                                "\t\t\"Executeunite\": \"中国国际经济贸易仲裁委员会\",\n" +
                                "\t\t\"Yiwu\": \"未履行生效法律文书确定的义务\",\n" +
                                "\t\t\"Executestatus\": \"全部未履行\",\n" +
                                "\t\t\"Actionremark\": \"违反财产报告制度\",\n" +
                                "\t\t\"Publicdate\": \"2019-06-27 00:00:00\",\n" +
                                "\t\t\"Age\": 0,\n" +
                                "\t\t\"Sexy\": \"\",\n" +
                                "\t\t\"Updatedate\": \"\",\n" +
                                "\t\t\"Executeno\": \"（2019）中国贸仲京裁字第0008号\",\n" +
                                "\t\t\"Performedpart\": \"暂无\",\n" +
                                "\t\t\"Unperformpart\": \"暂无\",\n" +
                                "\t\t\"OrgType\": \"2\",\n" +
                                "\t\t\"OrgTypeName\": \"失信企业\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"27c2e5524bcf597f308e3fb88fce63772\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Uniqueno\": \"\",\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-02-18 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)鄂01执291号\",\n" +
                                "\t\t\"Orgno\": \"58255643-8\",\n" +
                                "\t\t\"Ownername\": \"\",\n" +
                                "\t\t\"Executegov\": \"武汉市中级人民法院\",\n" +
                                "\t\t\"Province\": \"湖北\",\n" +
                                "\t\t\"Executeunite\": \"武汉市中级人民法院\",\n" +
                                "\t\t\"Yiwu\": \"(2017)鄂01民初3721号\",\n" +
                                "\t\t\"Executestatus\": \"全部未履行\",\n" +
                                "\t\t\"Actionremark\": \"被执行人无正当理由拒不履行执行和解协议\",\n" +
                                "\t\t\"Publicdate\": \"2019-10-15 00:00:00\",\n" +
                                "\t\t\"Age\": 0,\n" +
                                "\t\t\"Sexy\": \"\",\n" +
                                "\t\t\"Updatedate\": \"\",\n" +
                                "\t\t\"Executeno\": \"(2017)鄂01民初3721号\",\n" +
                                "\t\t\"Performedpart\": \"暂无\",\n" +
                                "\t\t\"Unperformpart\": \"暂无\",\n" +
                                "\t\t\"OrgType\": \"2\",\n" +
                                "\t\t\"OrgTypeName\": \"失信企业\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"1e0ce020b94f2b2c7b42792f0ae5425e2\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Uniqueno\": \"\",\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-02-18 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)鄂01执290号\",\n" +
                                "\t\t\"Orgno\": \"58255643-8\",\n" +
                                "\t\t\"Ownername\": \"\",\n" +
                                "\t\t\"Executegov\": \"武汉市中级人民法院\",\n" +
                                "\t\t\"Province\": \"湖北\",\n" +
                                "\t\t\"Executeunite\": \"武汉市中级人民法院\",\n" +
                                "\t\t\"Yiwu\": \"(2017)鄂01民初3720号\",\n" +
                                "\t\t\"Executestatus\": \"全部未履行\",\n" +
                                "\t\t\"Actionremark\": \"被执行人无正当理由拒不履行执行和解协议\",\n" +
                                "\t\t\"Publicdate\": \"2019-10-15 00:00:00\",\n" +
                                "\t\t\"Age\": 0,\n" +
                                "\t\t\"Sexy\": \"\",\n" +
                                "\t\t\"Updatedate\": \"\",\n" +
                                "\t\t\"Executeno\": \"(2017)鄂01民初3720号\",\n" +
                                "\t\t\"Performedpart\": \"暂无\",\n" +
                                "\t\t\"Unperformpart\": \"暂无\",\n" +
                                "\t\t\"OrgType\": \"2\",\n" +
                                "\t\t\"OrgTypeName\": \"失信企业\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"5b330901d77d1e9290c8e93befdfde742\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Uniqueno\": \"\",\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-02-18 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)鄂01执292号\",\n" +
                                "\t\t\"Orgno\": \"58255643-8\",\n" +
                                "\t\t\"Ownername\": \"\",\n" +
                                "\t\t\"Executegov\": \"武汉市中级人民法院\",\n" +
                                "\t\t\"Province\": \"湖北\",\n" +
                                "\t\t\"Executeunite\": \"武汉市中级人民法院\",\n" +
                                "\t\t\"Yiwu\": \"(2017)鄂01民初3719号\",\n" +
                                "\t\t\"Executestatus\": \"全部未履行\",\n" +
                                "\t\t\"Actionremark\": \"被执行人无正当理由拒不履行执行和解协议\",\n" +
                                "\t\t\"Publicdate\": \"2019-10-15 00:00:00\",\n" +
                                "\t\t\"Age\": 0,\n" +
                                "\t\t\"Sexy\": \"\",\n" +
                                "\t\t\"Updatedate\": \"\",\n" +
                                "\t\t\"Executeno\": \"(2017)鄂01民初3719号\",\n" +
                                "\t\t\"Performedpart\": \"暂无\",\n" +
                                "\t\t\"Unperformpart\": \"暂无\",\n" +
                                "\t\t\"OrgType\": \"2\",\n" +
                                "\t\t\"OrgTypeName\": \"失信企业\"\n" +
                                "\t}],\n" +
                                "\t\"Status\": \"200\",\n" +
                                "\t\"Message\": \"查询成功\"\n" +
                                "}";

                        log.info("请求内容"+content);
                        ApiSearchShiXin apiSearchShiXin = gson.fromJson(content, ApiSearchShiXin.class);
                        if (apiSearchShiXin != null &&null!=apiSearchShiXin.getResult()&& !apiSearchShiXin.getResult().isEmpty()) {
                            for(ApiSearchShiXin.Result result:apiSearchShiXin.getResult()){
                                CustomerRisk risk_code = customerRiskMapper.selectOne(new QueryWrapper<CustomerRisk>().eq("risk_code", result.getAnno()));
                                if (null!=risk_code){
                                    continue;
                                }
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskTime(new Date());
                                customerRisk.setRiskContent(new Gson().toJson(result));
//                                 风险案号
                                customerRisk.setRiskCode(result.getAnno());
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.ACTIONATLAW);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE4);
//                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_1);
                                /**
                                 * 风险详情发生时间
                                 */
                                customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getLiandate()));
//                                标记
                                customerRisk.setFalg("0");
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(SEARCHZHIXING)) {
//                      被执行人信息
                        log.info("开始失信被执行人");
                        urlName=urlName+"&searchKey="+customerName+"&isExactlySame=true";
//                        String content = HttpUtils.Get(urlName,reqHeader);
                        String content = "{\n" +
                                "\t\"OrderNumber\": \"COURT2019121110364568510399\",\n" +
                                "\t\"Paging\": {\n" +
                                "\t\t\"PageSize\": 10,\n" +
                                "\t\t\"PageIndex\": 1,\n" +
                                "\t\t\"TotalRecords\": 15\n" +
                                "\t},\n" +
                                "\t\"Result\": [{\n" +
                                "\t\t\"Id\": \"c7412c8604e1aff1fd45e202ece80f701\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-11-04 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京0105执43613号\",\n" +
                                "\t\t\"ExecuteGov\": \"北京市朝阳区人民法院\",\n" +
                                "\t\t\"Biaodi\": \"290000\",\n" +
                                "\t\t\"Status\": \"\",\n" +
                                "\t\t\"PartyCardNum\": \"58255643-8\",\n" +
                                "\t\t\"Updatedate\": \"\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"f12729fa8795870b914af3e1b47d464c1\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-10-30 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京01执1014号\",\n" +
                                "\t\t\"ExecuteGov\": \"北京市第一中级人民法院\",\n" +
                                "\t\t\"Biaodi\": \"153000\",\n" +
                                "\t\t\"Status\": \"\",\n" +
                                "\t\t\"PartyCardNum\": \"58255643-8\",\n" +
                                "\t\t\"Updatedate\": \"\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"791a939d99c1049a40620643e69c34b81\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-10-25 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京0105执42934号\",\n" +
                                "\t\t\"ExecuteGov\": \"北京市朝阳区人民法院\",\n" +
                                "\t\t\"Biaodi\": \"1078395\",\n" +
                                "\t\t\"Status\": \"\",\n" +
                                "\t\t\"PartyCardNum\": \"58255643-8\",\n" +
                                "\t\t\"Updatedate\": \"\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"c934068ae1c4065dda21b4358aa03ce81\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-10-10 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京01执恢337号\",\n" +
                                "\t\t\"ExecuteGov\": \"北京市第一中级人民法院\",\n" +
                                "\t\t\"Biaodi\": \"109423768.0\",\n" +
                                "\t\t\"Status\": \"\",\n" +
                                "\t\t\"PartyCardNum\": \"58255643-8\",\n" +
                                "\t\t\"Updatedate\": \"\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"7a5653edd63261e1d53604cd8a2cc4fe1\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-09-16 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京0105执38092号\",\n" +
                                "\t\t\"ExecuteGov\": \"北京市朝阳区人民法院\",\n" +
                                "\t\t\"Biaodi\": \"9360920\",\n" +
                                "\t\t\"Status\": \"\",\n" +
                                "\t\t\"PartyCardNum\": \"58255643-8\",\n" +
                                "\t\t\"Updatedate\": \"\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"f4d3424d4304da21cf56533021cb0e0e1\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-07-26 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京01执740号\",\n" +
                                "\t\t\"ExecuteGov\": \"北京市第一中级人民法院\",\n" +
                                "\t\t\"Biaodi\": \"202863\",\n" +
                                "\t\t\"Status\": \"\",\n" +
                                "\t\t\"PartyCardNum\": \"58255643-8\",\n" +
                                "\t\t\"Updatedate\": \"\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"f1c1f37d9a98c17fc8c6cc7e645f99f61\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-06-14 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京03执781号\",\n" +
                                "\t\t\"ExecuteGov\": \"北京市第三中级人民法院\",\n" +
                                "\t\t\"Biaodi\": \"1641853310.0\",\n" +
                                "\t\t\"Status\": \"\",\n" +
                                "\t\t\"PartyCardNum\": \"91110105582556438-3\",\n" +
                                "\t\t\"Updatedate\": \"\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"887b236071d997e6f16dd8dfc020ec1f1\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-06-13 00:00:00\",\n" +
                                "\t\t\"Anno\": \"（2019）沪01执816号\",\n" +
                                "\t\t\"ExecuteGov\": \"上海市第一中级人民法院\",\n" +
                                "\t\t\"Biaodi\": \"533053760.0\",\n" +
                                "\t\t\"Status\": \"\",\n" +
                                "\t\t\"PartyCardNum\": \"91110105582****4383\",\n" +
                                "\t\t\"Updatedate\": \"\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"e38e03518bb5d10e8040784ea4a569291\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-04-26 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京0105执17154号\",\n" +
                                "\t\t\"ExecuteGov\": \"北京市朝阳区人民法院\",\n" +
                                "\t\t\"Biaodi\": \"6755000\",\n" +
                                "\t\t\"Status\": \"\",\n" +
                                "\t\t\"PartyCardNum\": \"58255643-8\",\n" +
                                "\t\t\"Updatedate\": \"\"\n" +
                                "\t}, {\n" +
                                "\t\t\"Id\": \"0dd8366ae3e13a77666981829507455e1\",\n" +
                                "\t\t\"Sourceid\": 0,\n" +
                                "\t\t\"Name\": \"乐视控股（北京）有限公司\",\n" +
                                "\t\t\"Liandate\": \"2019-01-16 00:00:00\",\n" +
                                "\t\t\"Anno\": \"(2019)京0105执2247号\",\n" +
                                "\t\t\"ExecuteGov\": \"北京市朝阳区人民法院\",\n" +
                                "\t\t\"Biaodi\": \"1726095\",\n" +
                                "\t\t\"Status\": \"\",\n" +
                                "\t\t\"PartyCardNum\": \"58255643-8\",\n" +
                                "\t\t\"Updatedate\": \"\"\n" +
                                "\t}],\n" +
                                "\t\"Status\": \"200\",\n" +
                                "\t\"Message\": \"查询成功\"\n" +
                                "}";
                        log.info("请求内容"+content);
                        ApiSearchZhiXing apiSearchShiXin = gson.fromJson(content, ApiSearchZhiXing.class);
                        if (apiSearchShiXin != null&&null!=apiSearchShiXin.getResult() && !apiSearchShiXin.getResult().isEmpty()) {
                            for(ApiSearchZhiXing.Result result:apiSearchShiXin.getResult()){
                                CustomerRisk risk_code = customerRiskMapper.selectOne(new QueryWrapper<CustomerRisk>().eq("risk_code", result.getAnno()));
                                if (null!=risk_code){
                                    continue;
                                }
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskTime(new Date());
                                customerRisk.setRiskCode(result.getAnno());
                                customerRisk.setRiskContent(new Gson().toJson(result));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.ACTIONATLAW);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE5);
//                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_1);
                                /**
                                 * 风险详情发生时间
                                 */
                                customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getLiandate()));
                                customerRisk.setFalg("0");
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETJUDICIALASSISTANCE)){
//                        股权冻结
                        log.info("开始股权冻结监控");
                        urlName=urlName+"&searchKey="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiGetJudicialAssistance apiGetJudicialAssistance = gson.fromJson(content, ApiGetJudicialAssistance.class);
                        if (apiGetJudicialAssistance != null&&null!=apiGetJudicialAssistance.getResults() && !apiGetJudicialAssistance.getResults().isEmpty()) {
                            for(ApiGetJudicialAssistance.Result result:apiGetJudicialAssistance.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskTime(new Date());
                                customerRisk.setRiskCode(result.getEquityFreezeDetail().getExecutionVerdictNum());
                                customerRisk.setRiskContent(new Gson().toJson(result));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.ACTIONATLAW);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE6);
                                //                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_1);
                                if (null!=result.getEquityFreezeDetail()&& StringUtils.isNotBlank(result.getEquityFreezeDetail().getFreezeStartDate())){
                                    customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getEquityFreezeDetail().getFreezeStartDate()));
                                }
                                customerRisk.setFalg("0");
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(SEARCHCOURTANNOUNCEMENT)){
//                        法院公告
                        log.info("开始法院公告监控");
                        urlName=urlName+"&keyWord="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiSearchCourtAnnouncement apiSearchCourtAnnouncement = gson.fromJson(content, ApiSearchCourtAnnouncement.class);
                        if (apiSearchCourtAnnouncement != null&&null!=apiSearchCourtAnnouncement.getResults() && !apiSearchCourtAnnouncement.getResults().isEmpty()) {
                            for(ApiSearchCourtAnnouncement.Result result:apiSearchCourtAnnouncement.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskTime(new Date());
                                customerRisk.setRiskContent(new Gson().toJson(result));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.ACTIONATLAW);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE7);
                                //                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_0);

                                customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getPublishedDate()));
                                customerRisk.setFalg("0");
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(CASEFILING)){
//                      立案信息 (去掉)
                        log.info("开始立案信息监控");
                        urlName=urlName+"&searchKey="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiCaseFiling apiCaseFiling = gson.fromJson(content, ApiCaseFiling.class);
                        if (apiCaseFiling != null&&null!=apiCaseFiling.getResults() && !apiCaseFiling.getResults().isEmpty()) {
                            for(ApiCaseFiling.Result result:apiCaseFiling.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskTime(new Date());
                                customerRisk.setRiskContent(new Gson().toJson(result));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.ACTIONATLAW);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE8);
                                //                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_0);

                                customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getPublishDate()));
                                customerRisk.setFalg("0");
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(SEARCHCOURTNOTICE)){
//                        开庭公告
                        log.info("开始开庭公告监控");
                        urlName=urlName+"&searchKey="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiSearchCourtNotice apiSearchCourtNotice = gson.fromJson(content, ApiSearchCourtNotice.class);
                        if (apiSearchCourtNotice != null&&null!=apiSearchCourtNotice.getResults() && !apiSearchCourtNotice.getResults().isEmpty()) {
                            for(ApiSearchCourtNotice.Result result:apiSearchCourtNotice.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskTime(new Date());
                                customerRisk.setRiskContent(new Gson().toJson(result));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.ACTIONATLAW);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE9);
                                //                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_0);

                                customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getLianDate()));
                                customerRisk.setFalg("0");
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETJUDICIALSALELIST)){
//                      司法拍卖
                        log.info("开始司法拍卖监控");
                        urlName=urlName+"&keyWord="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiGetJudicialSaleList apiGetJudicialSaleList = gson.fromJson(content, ApiGetJudicialSaleList.class);
                        if (apiGetJudicialSaleList != null&&null!=apiGetJudicialSaleList.getResults() && !apiGetJudicialSaleList.getResults().isEmpty()) {
                            for(ApiGetJudicialSaleList.Result result:apiGetJudicialSaleList.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(new Gson().toJson(result));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE10);
                                //                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_0);

                                customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getActionRemark()));
                                customerRisk.setFalg("0");
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETLANDMORTGAGELIST)){
//                      土地抵押接口
                        log.info("开始土地抵押接口监控");
                        urlName=urlName+"&keyWord="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiGetLandMortgageList apiGetLandMortgageList = gson.fromJson(content, ApiGetLandMortgageList.class);
                        if (apiGetLandMortgageList != null&&null!=apiGetLandMortgageList.getResults() && !apiGetLandMortgageList.getResults().isEmpty()) {
                            for(ApiGetLandMortgageList.Result result:apiGetLandMortgageList.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(new Gson().toJson(result));
                                customerRisk.setRiskTime(new Date());
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE11);
                                //                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_0);

                                customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getStartDate()));
                                customerRisk.setFalg("0");
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETENVPUNISHMENTLIST)){
//                        环保处罚接口
                        log.info("开始环保处罚接口监控");
                        urlName=urlName+"&keyWord="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiGetEnvPunishmentList apiGetEnvPunishmentList = gson.fromJson(content, ApiGetEnvPunishmentList.class);
                        if (apiGetEnvPunishmentList != null&&null!=apiGetEnvPunishmentList.getResults() && !apiGetEnvPunishmentList.getResults().isEmpty()) {
                            for(ApiGetEnvPunishmentList.Result result:apiGetEnvPunishmentList.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(new Gson().toJson(result));
                                customerRisk.setRiskTime(DateUtil.parseDateTime(result.getPunishDate()));
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE12);
                                //                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_0);

                                customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getPunishDate()));
                                customerRisk.setFalg("0");
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETCHATTELMORTGAGE)){
//                        动产抵押查询
                        log.info("开始动产抵押查询监控");
                        urlName=urlName+"&keyWord="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiGetChattelMortgage apiGetChattelMortgage = gson.fromJson(content, ApiGetChattelMortgage.class);
                        if (apiGetChattelMortgage != null &&null!=apiGetChattelMortgage.getResults()&& !apiGetChattelMortgage.getResults().isEmpty()) {
                            for(ApiGetChattelMortgage.Result result:apiGetChattelMortgage.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(new Gson().toJson(result));
                                customerRisk.setRiskTime(new Date());
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE13);
                                //                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_0);

                                customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getPublicDate()));
                                customerRisk.setFalg("0");
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETSERIOUSVIOLATIONLIST)){
//                        严重违法
                        log.info("开始严重违法监控");
                        urlName=urlName+"&keyWord="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiGetSeriousViolationList apiGetSeriousViolationList = gson.fromJson(content, ApiGetSeriousViolationList.class);
                        if (apiGetSeriousViolationList != null&&null!=apiGetSeriousViolationList.getResults() && !apiGetSeriousViolationList.getResults().isEmpty()) {
                            for(ApiGetSeriousViolationList.Result result:apiGetSeriousViolationList.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(new Gson().toJson(result));
                                customerRisk.setRiskTime(new Date());
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE14);
                                //                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_1);

                                customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getAddDate()));
                                customerRisk.setFalg("0");
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETTAXOWENOTICELIST)){
//                        欠税公告
                        log.info("开始欠税公告监控");
                        urlName=urlName+"&keyWord="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiGetTaxOweNoticeList apiGetTaxOweNoticeList = gson.fromJson(content, ApiGetTaxOweNoticeList.class);
                        if (apiGetTaxOweNoticeList != null &&null!=apiGetTaxOweNoticeList.getResults() && !apiGetTaxOweNoticeList.getResults().isEmpty()) {
                            for(ApiGetTaxOweNoticeList.Result result:apiGetTaxOweNoticeList.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(new Gson().toJson(result));
                                customerRisk.setRiskTime(new Date());
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE15);
                                //                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_0);

                                customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getPublishDate()));
                                customerRisk.setFalg("0");
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(TAXILLEGALLIST)){
//                        欠税公告
                        log.info("开始税收违法监控");
                        urlName=urlName+"&searchKey="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiTaxIllegalList apiTaxIllegalList = gson.fromJson(content, ApiTaxIllegalList.class);
                        if (apiTaxIllegalList != null&&null!=apiTaxIllegalList.getResults() && !apiTaxIllegalList.getResults().isEmpty()) {
                            for(ApiTaxIllegalList.Result result:apiTaxIllegalList.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(new Gson().toJson(result));
                                customerRisk.setRiskTime(new Date());
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE16);
                                //                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_0);
                                customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getIllegalTime()));
                                customerRisk.setFalg("0");
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETGSADMINISTRATIVEPENALTYLIST)){
//                        行政处罚
                        urlName=urlName+"&searchKey="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiGetGsAdministrativePenaltyList apiGetGsAdministrativePenaltyList =
                                gson.fromJson(content, ApiGetGsAdministrativePenaltyList.class);
                        if (apiGetGsAdministrativePenaltyList != null&&null!=apiGetGsAdministrativePenaltyList.getResults()  && !apiGetGsAdministrativePenaltyList.getResults().isEmpty()) {
                            for(ApiGetGsAdministrativePenaltyList.Result result:apiGetGsAdministrativePenaltyList.getResults()){
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(new Gson().toJson(result));
                                customerRisk.setRiskTime(new Date());
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE17);
                                //                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_0);

                                customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getPublicDate()));
                                customerRisk.setFalg("0");
                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }else if (urlName.contains(GETOPEXCEPTION)){
//                      企业经营异常信息
                        urlName=urlName+"&keyNo="+customerName;
                        String content = HttpUtils.Get(urlName,reqHeader);
                        log.info("请求内容"+content);
                        ApiGetOpException apiGetOpException =
                                gson.fromJson(content, ApiGetOpException.class);
                        if (apiGetOpException != null&&null!=apiGetOpException.getResults() && !apiGetOpException.getResults().isEmpty()) {
                            for(ApiGetOpException.Result result:apiGetOpException.getResults()) {
                                CustomerRisk customerRisk = setCustomerRiskDate(customerUserInfo);
                                customerRisk.setRiskContent(new Gson().toJson(result));
                                customerRisk.setRiskTime(new Date());
//                                风险大类
                                customerRisk.setRiskType(RiskWarnConstants.OPERATINGRISK);
//                                风险小类
                                customerRisk.setRiskMType(RiskWarnConstants.TYPE18);
                                //                                风险级别
                                customerRisk.setRiskLevel(RiskWarnConstants.RISKLEVEL_0);
                                customerRisk.setRiskDetailTime(DateUtil.parseDate(result.getAddDate()));
                                customerRisk.setFalg("0");

                                customerRiskMapper.insert(customerRisk);
                            }
                        }
                    }
                    log.info(urlName+"结束监控");
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
        return new String[] { DigestUtils.md5Hex(APPKEY.concat(timeSpan).concat(ApiConstants.SECKEY)).toUpperCase(), timeSpan };
    }
}
