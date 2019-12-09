package com.ryuntech.saas.outcontroller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.gson.Gson;
import com.ryuntech.common.utils.HttpUtils;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.CustomerUserInfoDTO;
import com.ryuntech.saas.api.form.CustomerMonitorForm;
import com.ryuntech.saas.api.form.CustomerUserInfoForm;
import com.ryuntech.saas.api.helper.constant.ApiConstants;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.ICustomerMonitorService;
import com.ryuntech.saas.api.service.ICustomerUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;
import static com.ryuntech.saas.api.helper.constant.ApiConstants.APPKEY;

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


    @PostMapping("/outaddlist")
    @ApiOperation(value = "分页、条件查询客户列表信息")
    @ApiImplicitParam(name = "customerUserInfo", value = "查询条件", required = true, dataType = "CustomerUserInfo", paramType = "body")
    public Result<IPage<CustomerUserInfo>> list(@RequestBody CustomerUserInfoForm customerUserInfoForm, QueryPage queryPage) {
        log.info("customerUserInfoForm.getCustomerId()"+customerUserInfoForm.getCustomerId());
//        查询非监控列表的数据
        customerUserInfoForm.setIsRisk(1);
        return  iCustomerUserInfoService.selectPageList(customerUserInfoForm, queryPage);
    }

    /**
     * 获取Auth Code
     * @return
     */
    protected static final String[] randomAuthentHeader() {
        String timeSpan = String.valueOf(System.currentTimeMillis() / 1000);
        return new String[] { DigestUtils.md5Hex(APPKEY.concat(timeSpan).concat(ApiConstants.SECKEY)).toUpperCase(), timeSpan };
    }
    /**
     * 添加合同跟进信息
     *
     * @param customerMonitorForm
     * @return
     */
    @PostMapping("/outadd")
    @ApiOperation(value = "添加客户监控信息")
    @ApiImplicitParam(name = "customerMonitorForm", value = "客户监控信息", required = true, dataType = "CustomerMonitorForm", paramType = "body")
    public Result add(@RequestBody CustomerMonitorForm customerMonitorForm) throws Exception {
        if (null!=customerMonitorForm){
            List<String> customerIds = customerMonitorForm.getCustomerIds();
            for (String customerId:customerIds){
//                判断该企业是否已经存在
                CustomerMonitor customerMonitor = iCustomerMonitorService.getOne(new QueryWrapper<CustomerMonitor>().eq("customer_id", customerId));
                if (null!=customerMonitor){
                    log.info("该公司已经在监控列表中"+customerMonitor.getMonitorId());
                    continue;
                }

//                判断该公司是否存在企查查中

                CustomerUserInfoDTO customerUserInfoDTO = iCustomerUserInfoService.selectCustomerUserInfoDTO(new CustomerUserInfo().setCustomerId(customerId));

                String geteciImage = ApiConstants.SEARCH+"?key="+APPKEY;

                String[] autherHeader = randomAuthentHeader();
                HashMap<String, String> reqHeader = new HashMap<>();
                reqHeader.put("Token",autherHeader[0]);
                reqHeader.put("Timespan",autherHeader[1]);
                Gson gson = new Gson();
                String customerName = customerUserInfoDTO.getCustomerName();
                String urlName=geteciImage+"&keyword="+customerName;
                String content = HttpUtils.Get(urlName,reqHeader);
                ApiGetEciImage apiGetEciImage = gson.fromJson(content, ApiGetEciImage.class);
                if (null==apiGetEciImage){
                    return new Result();
                }
                if (apiGetEciImage != null && StringUtils.isNotBlank(apiGetEciImage.getStatus())) {
                    String status = apiGetEciImage.getStatus();
                    if (status.equals("201")){
                        return new Result(OPERATE_ERROR,"没有查询到对应的公司");
                    }
                    if (status.equals("214")){
                        return new Result(OPERATE_ERROR,"您还未购买过该接口，请先购买");
                    }
                }

                String  monitorId = String.valueOf(generateId());
                CustomerMonitor customerM = new CustomerMonitor();
                customerM.setMonitorId(monitorId);
                customerM.setCustomerId(customerId);
                customerM.setCreated(new Date());
                customerM.setUpdated(new Date());
                customerM.setStaffId(customerUserInfoDTO.getStaffId());
                customerM.setStaffName(customerUserInfoDTO.getStaffName());
                customerM.setCustomerName(customerUserInfoDTO.getCustomerName());
                iCustomerMonitorService.saveOrUpdate(customerM);
            }
        }
        return new Result();
    }
}
