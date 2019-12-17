package com.ryuntech.saas.outcontroller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.*;
import com.ryuntech.saas.api.form.CustomerRiskForm;
import com.ryuntech.saas.api.form.CustomerUserInfoForm;
import com.ryuntech.saas.api.helper.constant.RiskWarnConstants;
import com.ryuntech.saas.api.model.CustomerMonitor;
import com.ryuntech.saas.api.model.CustomerRisk;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author EDZ
 */
@Slf4j
@RestController
@RequestMapping("/minicustomerrisk")
@Api(value = "MiniCustomerRiskController", tags = {"小程序登录接口"})
public class MiniCustRiskController {

    @Autowired
    private ICustomerRiskService iCustomerRiskService;

    @Autowired
    private ICustomerUserInfoService iCustomerUserInfoService;


    @Autowired
    private IReceivableContractService iReceivableContractService;

    @Autowired
    private IEmployeeService  iEmployeeService;


    @PostMapping("/outlist")
    @ApiOperation(value = "分页、条件查询用户列表信息")
    @ApiImplicitParam(name = "customerRiskForm", value = "查询条件", required = true, dataType = "CustomerRiskForm", paramType = "body")
    public Result monitorList(@RequestHeader String EmployeeId,@RequestBody CustomerRiskForm customerRiskForm, QueryPage queryPage) {
        log.info("EmployeeId"+EmployeeId);
        log.info("customerRiskForm.getCustomerId()"+customerRiskForm.getCustomerId());
        if (StringUtils.isBlank(EmployeeId)){
            return new Result("员工编号不能为空");
        }

        List<String> customerRiskDTOS = iCustomerRiskService.selectGroupConcatByTime(customerRiskForm);

        if (customerRiskDTOS.isEmpty()){
            return new Result();
        }

        List<CustomerRiskListDTO> customerRiskList = new ArrayList<>();
        Integer totalFlagLength=0;
        for (String riskTime :customerRiskDTOS){
            List<CustomerRiskDTO> customerRiskD = iCustomerRiskService.selectGroupConcat(
                    new CustomerRiskForm().setRiskTime(riskTime).setRiskDetailTime(riskTime));
            if (customerRiskD.isEmpty()){
                continue;
            }
            CustomerRiskListDTO customerRiskListDTO = new CustomerRiskListDTO();
            customerRiskListDTO.setRiskTime(riskTime);
            List< CustomerRiskListDTO.OnCompany> onCompanyList = new ArrayList<>();
            customerRiskListDTO.setRiskLength(customerRiskD.size()+"");
            Integer flagLength=0;
            for (CustomerRiskDTO customerR :customerRiskD) {
                String riskTypes = customerR.getRiskTypes();
                if (StringUtils.isNotBlank(riskTypes)) {
                    String[] split = riskTypes.split(",");
                    HashMap<String, String> typeMap = new HashMap<>();
                    for (String ty : split) {
                        int length = 1;
                        if (!StringUtils.isBlank(typeMap.get(ty))) {
                            length += Integer.parseInt(typeMap.get(ty));
                        }
                        typeMap.put(ty, length + "");
                    }

                    /**
                     * 判断未读已读
                     */
                    String falg = customerR.getFalg();
                    if (StringUtils.isNotBlank(falg)&&falg.equals("0")){
                        flagLength++;
                    }

                    CustomerRiskListDTO.OnCompany onCompany = new CustomerRiskListDTO.OnCompany();

                    onCompany.setCustomerName(customerR.getCustomerName());
                    if (StringUtils.isNotBlank(typeMap.get(RiskWarnConstants.INDUSTRIALANDCOMMERCIALCHANGE))){
                        onCompany.setIndustrialandcommercial("工商变更");
                        onCompany.setIndustrialandLength(typeMap.get(RiskWarnConstants.INDUSTRIALANDCOMMERCIALCHANGE));
                    }else {
                        onCompany.setIndustrialandcommercial("工商变更");
                        onCompany.setIndustrialandLength("0");
                    }

                    if (StringUtils.isNotBlank(typeMap.get(RiskWarnConstants.ACTIONATLAW))){
                        onCompany.setActionatlaw("法律诉讼");
                        onCompany.setActionatlawLength(typeMap.get(RiskWarnConstants.ACTIONATLAW));
                    }else {
                        onCompany.setActionatlaw("法律诉讼");
                        onCompany.setActionatlawLength("0");
                    }

                    if (StringUtils.isNotBlank(typeMap.get(RiskWarnConstants.OPERATINGRISK))){
                        onCompany.setOperatingrisk("经营风险");
                        onCompany.setOperatingriskLength(typeMap.get(RiskWarnConstants.OPERATINGRISK));
                    }else {
                        onCompany.setOperatingrisk("经营风险");
                        onCompany.setOperatingriskLength("0");
                    }
                    onCompanyList.add(onCompany);
                }
            }
            customerRiskListDTO.setOnCompanyList(onCompanyList);

            totalFlagLength+=flagLength;
            customerRiskListDTO.setFalgLength(flagLength.toString());
            customerRiskList.add(customerRiskListDTO);
        }
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("customerRiskList",customerRiskList);
        objectObjectHashMap.put("totalFlagLength",totalFlagLength);
        Employee byId = iEmployeeService.getById(EmployeeId);
        if (null!=byId){
                objectObjectHashMap.put("isWeChat",byId.getIsWeChat());
                objectObjectHashMap.put("isEmall",byId.getIsEmail());
                objectObjectHashMap.put("emall",byId.getEmail());
        }

        return new Result(objectObjectHashMap);
    }


    @PostMapping("/outwxlist")
    @ApiOperation(value = "分页、条件查询预警详情微信列表信息")
    @ApiImplicitParam(name = "customerRiskForm", value = "查询条件", required = true, dataType = "CustomerRiskForm", paramType ="body")
    public Result wxList(@RequestBody CustomerRiskForm customerRiskForm) {
        return new Result();
    }


    @PostMapping("/outdetaillist")
    @ApiOperation(value = "分页、条件查询预警详情列表信息")
    @ApiImplicitParam(name = "customerRiskForm", value = "查询条件", required = true, dataType = "CustomerRiskForm", paramType ="body")
    public Result detailList(@RequestBody CustomerRiskForm customerRiskForm) {
        log.info("detailList");
        //判断查询类型
        List<String> riskMTypes= customerRiskForm.getRiskMTypes();
        log.info("riskMTypes"+riskMTypes);
        /**
         * 工商变更全部：businessAll 法律诉讼全部：legalAll 经营风险全部：manageAll
         */
        if (riskMTypes.contains("businessAll")){
            riskMTypes.remove("businessAll");
            riskMTypes.add(RiskWarnConstants.TYPE1);
            riskMTypes.add(RiskWarnConstants.TYPE2);
            riskMTypes.add(RiskWarnConstants.TYPE3);
        }
        if (riskMTypes.contains("legalAll")){
            riskMTypes.add(RiskWarnConstants.TYPE4);
            riskMTypes.add(RiskWarnConstants.TYPE5);
            riskMTypes.add(RiskWarnConstants.TYPE6);
            riskMTypes.add(RiskWarnConstants.TYPE7);
            riskMTypes.add(RiskWarnConstants.TYPE8);
            riskMTypes.add(RiskWarnConstants.TYPE9);
        }
        if (riskMTypes.contains("manageAll")){
            riskMTypes.add(RiskWarnConstants.TYPE10);
            riskMTypes.add(RiskWarnConstants.TYPE11);
            riskMTypes.add(RiskWarnConstants.TYPE12);
            riskMTypes.add(RiskWarnConstants.TYPE13);
            riskMTypes.add(RiskWarnConstants.TYPE14);
            riskMTypes.add(RiskWarnConstants.TYPE15);
            riskMTypes.add(RiskWarnConstants.TYPE16);
            riskMTypes.add(RiskWarnConstants.TYPE17);
            riskMTypes.add(RiskWarnConstants.TYPE18);
        }
        //开始解析时间
        String riskTimeType = customerRiskForm.getRiskTimeType();
        //查询月份 0 本月 1上个月 2本周 3上周 4自定义
        if (riskTimeType.equals("0")){
            String monthFirstDay = DateUtil.getMonthFirstDay(new Date());
            String monthLastDay = DateUtil.getMonthLastDay(new Date());
            customerRiskForm.setRiskStartTime(monthFirstDay);
            customerRiskForm.setRiskEndTime(monthLastDay);
        }else if (riskTimeType.equals("1")){
            String lastMonth = DateUtil.getLastMonth(new Date());
            String monthFirstDay = DateUtil.getMonthFirstDay(DateUtil.parseDateTime(lastMonth));
            String monthLastDay = DateUtil.getMonthLastDay(DateUtil.parseDateTime(lastMonth));
            customerRiskForm.setRiskStartTime(monthFirstDay);
            customerRiskForm.setRiskEndTime(monthLastDay);
        }else if (riskTimeType.equals("2")){
            String lastWe = DateUtil.getLastWe(new Date());
            customerRiskForm.setRiskStartTime(lastWe);
            customerRiskForm.setRiskEndTime(DateUtil.formatDateTime(new Date()));
        }else if (riskTimeType.equals("3")){
            String lastWe = DateUtil.getLastWe(new Date());
            Date date = DateUtil.parseDateTime(lastWe);
            customerRiskForm.setRiskStartTime(DateUtil.formatDateTime(date));
            customerRiskForm.setRiskEndTime(lastWe);
        }else if (riskTimeType.equals("4")){
            //自定义时间
        }

        //获取部门编号
        customerRiskForm.getDepartmentId();

        List<CustomerRiskDTO> customerRiskDTOS = iCustomerRiskService.selectGroupConcat(customerRiskForm);
        List<CustomerRiskDetailListDTO> customerRiskDetailListDTOS = new ArrayList<>();
        Integer totleDynamicSize= 0;
        for (CustomerRiskDTO customerRiskDTO :customerRiskDTOS){
            CustomerRiskDetailListDTO customerRiskDetailListDTO = new CustomerRiskDetailListDTO();
            String customerId = customerRiskDTO.getCustomerId();
            String customerName = customerRiskDTO.getCustomerName();
            customerRiskDetailListDTO.setCustomerId(customerId);
//            客户名称
            customerRiskDetailListDTO.setCustomerName(customerName);
//            查询负责员工
            CustomerUserInfo customerUserInfo = iCustomerUserInfoService.selectByCustomerF(new CustomerUserInfoForm().setCustomerId(customerId));
            if (null!=customerUserInfo){
//                负责员工
                customerRiskDetailListDTO.setStaffName(customerUserInfo.getStaffName()==null?"0.00":customerUserInfo.getStaffName());
            }else {
                customerRiskDetailListDTO.setStaffName("无");
            }
//            查询待收款
            String balanceAmounts = iReceivableContractService.selectSumByRContract(new ReceivableContractDTO().setCustomerId(customerId));
            customerRiskDetailListDTO.setBalanceAmount(balanceAmounts==null?"0.00":balanceAmounts);
//            查询详情数据，风险列表
            List<CustomerRisk> customerRisks = iCustomerRiskService.list(
                    new QueryWrapper<CustomerRisk>().
                            eq("customer_id", customerId).groupBy("risk_mtype"));
            List<CustomerRiskDetailListDTO.RiskListDetail> riskListDetails = new ArrayList<>();
            for (CustomerRisk customerRisk:customerRisks){
                CustomerRiskDetailListDTO.RiskListDetail riskListDetail = new CustomerRiskDetailListDTO.RiskListDetail();
//                风险类型
                String riskMType2 = customerRisk.getRiskMType();
                riskListDetail.setRiskType(riskMType2);
                switch (riskMType2){
                    case RiskWarnConstants.TYPE1:
                        riskListDetail.setRiskName("法人变更");
                        break;
                    case RiskWarnConstants.TYPE2:
                        riskListDetail.setRiskName("公司名称变更");
                        break;
                    case RiskWarnConstants.TYPE3:
                        riskListDetail.setRiskName("大股东变更");
                        break;
                    case RiskWarnConstants.TYPE4:
                        riskListDetail.setRiskName("失信被执行人");
                        break;
                    case RiskWarnConstants.TYPE5:
                        riskListDetail.setRiskName("执行人");
                        break;
                    case RiskWarnConstants.TYPE6:
                        riskListDetail.setRiskName("股权冻结");
                        break;
                    case RiskWarnConstants.TYPE7:
                        riskListDetail.setRiskName("法院公告");
                        break;
                    case RiskWarnConstants.TYPE8:
                        riskListDetail.setRiskName("立案消息");
                        break;
                    case RiskWarnConstants.TYPE10:
                        riskListDetail.setRiskName("司法拍卖");
                        break;
                    case RiskWarnConstants.TYPE11:
                        riskListDetail.setRiskName("土地抵押");
                        break;
                    case RiskWarnConstants.TYPE12:
                        riskListDetail.setRiskName("环保处罚");
                        break;
                    case RiskWarnConstants.TYPE13:
                        riskListDetail.setRiskName("动产抵押");
                        break;
                    case RiskWarnConstants.TYPE14:
                        riskListDetail.setRiskName("严重违法");
                        break;
                    case RiskWarnConstants.TYPE15:
                        riskListDetail.setRiskName("欠税公告");
                        break;
                    case RiskWarnConstants.TYPE16:
                        riskListDetail.setRiskName("税收违法");
                        break;
                    case RiskWarnConstants.TYPE17:
                        riskListDetail.setRiskName("行政处罚");
                        break;
                    case RiskWarnConstants.TYPE18:
                        riskListDetail.setRiskName("企业经营异常");
                        break;
                }
//                风险级别
                riskListDetail.setRiskLevel(customerRisk.getRiskLevel());
//                查询当前客户当前风险类型的条数
                List<CustomerRisk> list = iCustomerRiskService.list(
                        new QueryWrapper<CustomerRisk>()
                                .eq("customer_id", customerId).eq("risk_mtype", riskMType2));
                riskListDetail.setRiskSize(String.valueOf(list.size()));

                ArrayList<CustomerRiskDetailListDTO.RiskListDetail.RiskListDetail2> riskListDetail2s = new ArrayList<>();
                for (CustomerRisk customerRisk1:list){
                    CustomerRiskDetailListDTO.RiskListDetail.RiskListDetail2 riskListDetail2 = new CustomerRiskDetailListDTO.RiskListDetail.RiskListDetail2();
                    riskListDetail2.setRiskContent(customerRisk1.getRiskContent());
                    riskListDetail2.setRiskCode(customerRisk1.getRiskCode());
                    riskListDetail2.setKeyNo(customerRisk1.getKeyNo());
                    riskListDetail2.setRiskTime(DateUtil.formatDate(customerRisk1.getRiskTime()));
                    riskListDetail2s.add(riskListDetail2);
                    totleDynamicSize++;
                }
                riskListDetail.setRiskListDetails2(riskListDetail2s);
                riskListDetails.add(riskListDetail);
            }
            customerRiskDetailListDTO.setRiskListDetails(riskListDetails);
            customerRiskDetailListDTOS.add(customerRiskDetailListDTO);
        }

        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("customerRLists",customerRiskDetailListDTOS);
        objectObjectHashMap.put("totleFalgLength",customerRiskDetailListDTOS.size());
        objectObjectHashMap.put("totleDynamicSize",totleDynamicSize);


        return new Result(objectObjectHashMap);
    }





}






























