package com.ryuntech.saas.outcontroller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.CustomerMonitorDTO;
import com.ryuntech.saas.api.dto.CustomerUserInfoDTO;
import com.ryuntech.saas.api.form.CustomerMonitorForm;
import com.ryuntech.saas.api.model.CustomerMonitor;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import com.ryuntech.saas.api.model.FollowupRecord;
import com.ryuntech.saas.api.service.ICustomerMonitorService;
import com.ryuntech.saas.api.service.ICustomerUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;

/**
 * @author EDZ
 */
@Slf4j
@RestController
@RequestMapping("/minimonitor")
@Api(value = "MiniMonitorController", tags = {"小程序登录接口"})
public class MiniMonitorController extends ModuleBaseController{
    @Autowired
    private ICustomerMonitorService iCustomerMonitorService;

    @Autowired
    private ICustomerUserInfoService iCustomerUserInfoService;



    @PostMapping("/outlist")
    @ApiOperation(value = "分页、条件查询用户列表信息")
    @ApiImplicitParam(name = "followupRecord", value = "查询条件", required = true, dataType = "FollowupRecord", paramType = "body")
    public Result<IPage<CustomerMonitor>> list(@RequestBody CustomerMonitor customerMonitor, QueryPage queryPage) {
        log.info("customerMonitor.getMonitorId()"+customerMonitor.getMonitorId());
        return  iCustomerMonitorService.pageList(customerMonitor, queryPage);
    }


    /**
     * 添加合同跟进信息
     *
     * @param customerMonitorForm
     * @return
     */
    @PostMapping("/outadd")
    @ApiOperation(value = "添加客户监控信息")
    @ApiImplicitParam(name = "customerMonitor", value = "客户监控信息", required = true, dataType = "CustomerMonitor", paramType = "body")
    public Result add(@RequestBody CustomerMonitorForm customerMonitorForm) {
        if (null!=customerMonitorForm){
            List<String> customerIds = customerMonitorForm.getCustomerIds();
            for (String customerId:customerIds){
                CustomerUserInfoDTO customerUserInfoDTO = iCustomerUserInfoService.selectCustomerUserInfoDTO(new CustomerUserInfo().setCustomerId(customerId));
                String  monitorId = String.valueOf(generateId());
                CustomerMonitor customerM = new CustomerMonitor();
                customerM.setMonitorId(monitorId);
                customerM.setCustomerId(customerId);
                customerM.setCreated(new Date());
                customerM.setUpdated(new Date());
                customerM.setCustomerName(customerUserInfoDTO.getCustomerName());
                iCustomerMonitorService.saveOrUpdate(customerM);
            }
        }
        return new Result();
    }
}
