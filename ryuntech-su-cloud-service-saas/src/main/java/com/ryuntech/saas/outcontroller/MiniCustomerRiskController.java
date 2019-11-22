package com.ryuntech.saas.outcontroller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.CustomerMonitorDTO;
import com.ryuntech.saas.api.dto.CustomerRiskDTO;
import com.ryuntech.saas.api.dto.CustomerRiskListDTO;
import com.ryuntech.saas.api.form.CustomerRiskForm;
import com.ryuntech.saas.api.helper.constant.RiskWarnConstants;
import com.ryuntech.saas.api.model.CustomerMonitor;
import com.ryuntech.saas.api.model.CustomerRisk;
import com.ryuntech.saas.api.service.ICustomerRiskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class MiniCustomerRiskController {

    @Autowired
    private ICustomerRiskService iCustomerRiskService;


    @PostMapping("/outlist")
    @ApiOperation(value = "分页、条件查询用户列表信息")
    @ApiImplicitParam(name = "customerRiskForm", value = "查询条件", required = true, dataType = "CustomerRiskForm", paramType = "body")
    public Result monitorList(@RequestBody CustomerRiskForm customerRiskForm, QueryPage queryPage) {
        log.info("customerRiskDTO.getCustomerId()"+customerRiskForm.getCustomerId());
        List<CustomerRiskDTO> customerRiskDTOS = iCustomerRiskService.selectGroupConcatByTime(new CustomerRiskForm());

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
}
