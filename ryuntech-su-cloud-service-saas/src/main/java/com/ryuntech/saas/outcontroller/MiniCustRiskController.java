package com.ryuntech.saas.outcontroller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import com.ryuntech.saas.api.service.ICustomerRiskService;
import com.ryuntech.saas.api.service.ICustomerUserInfoService;
import com.ryuntech.saas.api.service.IReceivableContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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


    @PostMapping("/outlist")
    @ApiOperation(value = "分页、条件查询用户列表信息")
    @ApiImplicitParam(name = "customerRiskForm", value = "查询条件", required = true, dataType = "CustomerRiskForm", paramType = "body")
    public Result monitorList(@RequestBody CustomerRiskForm customerRiskForm, QueryPage queryPage) {
        log.info("customerRiskDTO.getCustomerId()"+customerRiskForm.getCustomerId());
        List<CustomerRiskDTO> customerRiskDTOS = iCustomerRiskService.selectGroupConcatByTime(new CustomerRiskForm());

        if (customerRiskDTOS.isEmpty()){
            return new Result();
        }

        List<CustomerRiskListDTO> customerRiskList = new ArrayList<>();
        Integer totleFalgLength=0;
        for (CustomerRiskDTO customerRiskDTO :customerRiskDTOS){
            Date riskTime = customerRiskDTO.getRiskTime();
            List<CustomerRiskDTO> customerRiskD = iCustomerRiskService.selectGroupConcat(
                    new CustomerRiskForm().setRiskTime(DateUtil.formatDate(riskTime)));
            if (customerRiskD.isEmpty()){
                continue;
            }
            CustomerRiskListDTO customerRiskListDTO = new CustomerRiskListDTO();
            customerRiskListDTO.setRiskTime(DateUtil.formatDate(riskTime));
            List< CustomerRiskListDTO.OnCompany> onCompanyList = new ArrayList<>();
            customerRiskListDTO.setRiskLength(customerRiskD.size()+"");
            Integer falgLength=0;
            for (CustomerRiskDTO customerR :customerRiskD) {
                String riskTypes = customerR.getRiskTypes();
                if (StringUtils.isNotBlank(riskTypes)) {
                    String[] split = riskTypes.split(",");
                    HashMap<String, String> typeMap = new HashMap<>();
                    for (String ty : split) {
                        int length = 0;
                        if (StringUtils.isBlank(typeMap.get(ty))) {
                            length = 1;
                        } else {
                            length += Integer.parseInt(typeMap.get(ty));
                        }
                        typeMap.put(ty, length + "");
                    }

                    /**
                     * 判断未读已读
                     */
                    String falg = customerR.getFalg();
                    if (StringUtils.isNotBlank(falg)&&falg.equals("0")){
                        falgLength++;
                    }



                    CustomerRiskListDTO.OnCompany onCompany = new CustomerRiskListDTO.OnCompany();

                    onCompany.setCustomerName(customerR.getCustomerName());
                    if (StringUtils.isNotBlank(typeMap.get(RiskWarnConstants.INDUSTRIALANDCOMMERCIALCHANGE))){
                        onCompany.setIndustrialandcommercial("工商变更 " + typeMap.get(RiskWarnConstants.INDUSTRIALANDCOMMERCIALCHANGE));
                    }else {
                        onCompany.setIndustrialandcommercial("工商变更 0");
                    }

                    if (StringUtils.isNotBlank(typeMap.get(RiskWarnConstants.ACTIONATLAW))){
                        onCompany.setActionatlaw("法律诉讼 " + typeMap.get(RiskWarnConstants.ACTIONATLAW));
                    }else {
                        onCompany.setActionatlaw("法律诉讼 0");
                    }

                    if (StringUtils.isNotBlank(typeMap.get(RiskWarnConstants.OPERATINGRISK))){
                        onCompany.setOperatingrisk("经营风险 " + typeMap.get(RiskWarnConstants.OPERATINGRISK));
                    }else {
                        onCompany.setOperatingrisk("经营风险 0");
                    }
                    onCompanyList.add(onCompany);
                }
            }
            customerRiskListDTO.setOnCompanyList(onCompanyList);

            totleFalgLength+=falgLength;
            customerRiskListDTO.setFalgLength(falgLength.toString());
            customerRiskList.add(customerRiskListDTO);
        }
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("customerRiskList",customerRiskList);
        objectObjectHashMap.put("totleFalgLength",totleFalgLength);
        return new Result(objectObjectHashMap);
    }


    @PostMapping("/outdetaillist")
    @ApiOperation(value = "分页、条件查询预警详情列表信息")
    @ApiImplicitParam(name = "customerRiskForm", value = "查询条件", required = true, dataType = "CustomerRiskForm", paramType ="body")
    public Result detailList(@RequestBody CustomerRiskForm customerRiskForm) {
        log.info("detailList");
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
                customerRiskDetailListDTO.setStaffName(customerUserInfo.getStaffName());
            }
//            查询待收款
            String balanceAmounts = iReceivableContractService.selectSumByRContract(new ReceivableContractDTO().setCustomerId(customerId));
            customerRiskDetailListDTO.setBalanceAmount(balanceAmounts);
//            查询详情数据，风险列表
            List<CustomerRisk> customerRisks = iCustomerRiskService.list(
                    new QueryWrapper<CustomerRisk>().
                            eq("customer_id", customerId).groupBy("risk_type"));
            List<CustomerRiskDetailListDTO.RiskListDetail> riskListDetails = new ArrayList<>();
            for (CustomerRisk customerRisk:customerRisks){
                CustomerRiskDetailListDTO.RiskListDetail riskListDetail = new CustomerRiskDetailListDTO.RiskListDetail();
//                风险类型
                String riskType = customerRisk.getRiskType();
                if (riskType.equals("1")){
                    riskListDetail.setTiskType("法人变更");
                }
//                查询当前客户当前风险类型的条数
                List<CustomerRisk> list = iCustomerRiskService.list(
                        new QueryWrapper<CustomerRisk>()
                                .eq("customer_id", customerId).eq("risk_type", riskType));
                riskListDetail.setTiskSize(String.valueOf(list.size()));

                ArrayList<CustomerRiskDetailListDTO.RiskListDetail.RiskListDetail2> riskListDetail2s = new ArrayList<>();
                for (CustomerRisk customerRisk1:list){
                    CustomerRiskDetailListDTO.RiskListDetail.RiskListDetail2 riskListDetail2 = new CustomerRiskDetailListDTO.RiskListDetail.RiskListDetail2();
                    riskListDetail2.setRiskContent(customerRisk1.getRiskContent());
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





























