package com.ryuntech.saas.outcontroller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.gson.Gson;
import com.ryuntech.common.constant.enums.ExceptionEnum;
import com.ryuntech.common.exception.YkControllerException;
import com.ryuntech.common.utils.HttpUtils;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.CustomerUserInfoDTO;
import com.ryuntech.saas.api.form.CustomerMonitorForm;
import com.ryuntech.saas.api.form.CustomerUserInfoForm;
import com.ryuntech.saas.api.helper.constant.ApiConstants;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.ICompanyService;
import com.ryuntech.saas.api.service.ICustomerMonitorService;
import com.ryuntech.saas.api.service.ICustomerUserInfoService;
import com.ryuntech.saas.api.service.IEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @Autowired
    private IEmployeeService iEmployeeService;

    @Autowired
    private ICompanyService iCompanyService;



    @PostMapping("/outlist")
    @ApiOperation(value = "分页、条件查询用户列表信息")
    @ApiImplicitParam(name = "customerMonitor", value = "查询条件", required = true, dataType = "CustomerMonitor", paramType = "body")
    public Result<IPage<CustomerMonitor>> list(@RequestBody CustomerMonitor customerMonitor, QueryPage queryPage) {
        log.info("customerMonitor.getMonitorId()"+customerMonitor.getMonitorId());
        return  iCustomerMonitorService.pageList(customerMonitor, queryPage);
    }


    @PostMapping("/outaddlist")
    @ApiOperation(value = "分页、条件查询客户列表信息")
    @ApiImplicitParam(name = "customerUserInfoForm", value = "查询条件", required = true, dataType = "CustomerUserInfoForm", paramType = "body")
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

    @GetMapping("/outcancel")
    @ApiOperation(value = "添加客户监控信息")
    @ApiImplicitParam(name = "customerMonitorForm", value = "客户监控信息", required = true, dataType = "CustomerMonitorForm", paramType = "body")
    public Result cancel(CustomerMonitorForm customerMonitorForm) throws Exception {
        if (StringUtils.isBlank(customerMonitorForm.getMonitorId())){
            return new Result(OPERATE_ERROR,"监控编号为空，不能取消");
        }
        String monitorId = customerMonitorForm.getMonitorId();

        CustomerMonitor byId = iCustomerMonitorService.getById(monitorId);
        if (null==byId){
            return new Result(OPERATE_ERROR,"未查找到对应的监控对象不能取消");
        }
        byId.setStatus(false);
        boolean b = iCustomerMonitorService.updateById(byId);
        if (b){
            return new Result("取消成功");
        }else {
            return new Result("取消失败");
        }
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
    public Result add(@RequestBody CustomerMonitorForm customerMonitorForm) throws YkControllerException {
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

                String[] autherHeader = randomAuthentHeader();
                HashMap<String, String> reqHeader = new HashMap<>();
                reqHeader.put("Token", autherHeader[0]);
                reqHeader.put("Timespan", autherHeader[1]);
                Gson gson = new Gson();
                String urlName = ApiConstants.GETBASICDETAILSBYNAME + "?key=" + APPKEY + "&keyWord=" + customerUserInfoDTO.getCustomerName();
                String content = "";
                try {
                    content = HttpUtils.Get(urlName, reqHeader);
                }catch (Exception e){
                    throw new YkControllerException(ExceptionEnum.HTTP_ERROR);
                }

                ApiGetBasicDetailsByName apiGetBasicDetailsByName = gson.fromJson(content, ApiGetBasicDetailsByName.class);
                if (null==apiGetBasicDetailsByName){
                    return new Result();
                }
                if (apiGetBasicDetailsByName != null && StringUtils.isNotBlank(apiGetBasicDetailsByName.getStatus())) {
                    String status = apiGetBasicDetailsByName.getStatus();
                    if (status.equals("201")){
                        return new Result(OPERATE_ERROR,"没有查询到对应的公司");
                    }
                    if (status.equals("214")){
                        return new Result(OPERATE_ERROR,"您还未购买过该接口，请先购买");
                    }
                }
//                查询该客户对应的公司
                Company company = iCompanyService.selectByCompany(new Company().setCompanyId(customerUserInfoDTO.getCompanyId()));
                if (null!=company&&StringUtils.isBlank(company.getOperName())){
                    company.setOperName(apiGetBasicDetailsByName.getResult().getOperName());
                }
                company.setIsQichacha(true);
                iCompanyService.saveOrUpdate(company);

//                查询添加该监控的员工
                Employee employee = iEmployeeService.selectByEmployee(new Employee().setEmployeeId(customerMonitorForm.getEmployeeId()));

                String  monitorId = String.valueOf(generateId());
                CustomerMonitor customerM = new CustomerMonitor();
                customerM.setMonitorId(monitorId);
                customerM.setCustomerId(customerId);
                customerM.setCreated(new Date());
                customerM.setUpdated(new Date());
                /**
                 * 该客户的跟进人
                 */
                customerM.setStaffId(customerUserInfoDTO.getStaffId());
                customerM.setStaffName(customerUserInfoDTO.getStaffName());
                customerM.setCustomerName(customerUserInfoDTO.getCustomerName());
                /**
                 * 添加该客户的员工
                 */
                customerM.setEmployeeId(employee.getEmployeeId());
                customerM.setEmployeeName(employee.getName());
                customerM.setStatus(true);
                customerM.setIsWechat(false);
                customerM.setIsEmall(false);
                iCustomerMonitorService.saveOrUpdate(customerM);
            }
        }
        return new Result("添加成功");
    }



    /**
     * 更新监控状态
     *
     * @param customerMonitorForm
     * @return
     */
    @PostMapping("/outupdate")
    @ApiOperation(value = "更新监控状态")
    @ApiImplicitParam(name = "customerMonitorForm", value = "客户监控信息", required = true, dataType = "CustomerMonitorForm", paramType = "body")
    public Result update(@RequestBody CustomerMonitorForm customerMonitorForm) throws YkControllerException {
        String employeeId = customerMonitorForm.getEmployeeId();
        log.info("customerMonitorForm.getEmployeeId()"+employeeId);
        if (StringUtils.isBlank(employeeId)){
            return new Result(OPERATE_ERROR,"员工编号为空，不能更新");
        }
        CustomerMonitor customerMonitor =new CustomerMonitor();
        Boolean isWeChat = customerMonitorForm.getIsWeChat();
        customerMonitor.setIsWechat(isWeChat);
        Boolean isEmall = customerMonitorForm.getIsEmall();
        customerMonitor.setIsEmall(isEmall);
//        更新员工信息

        boolean b = iCustomerMonitorService.update(customerMonitor,
                new QueryWrapper<CustomerMonitor>().eq("employee_id",employeeId));
        Employee employee = iEmployeeService.selectByEmployee(new Employee().setEmployeeId(employeeId));
        employee.setIsWeChat(isWeChat);
        employee.setIsEmail(isEmall);
        boolean b1 = iEmployeeService.saveOrUpdate(employee);
        if (b&&b1){
            return new Result("更新成功");
        }else {
            return new Result(OPERATE_ERROR,"更新异常");
        }
    }
}
