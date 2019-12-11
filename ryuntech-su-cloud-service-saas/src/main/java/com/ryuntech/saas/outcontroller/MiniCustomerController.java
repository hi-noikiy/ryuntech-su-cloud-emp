package com.ryuntech.saas.outcontroller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.model.BaseDto;
import com.ryuntech.common.model.BaseForm;
import com.ryuntech.common.utils.CopyUtil;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.CustomerUserInfoDTO;
import com.ryuntech.saas.api.dto.ReceivableContractDTO;
import com.ryuntech.saas.api.form.CustomerUserInfoForm;
import com.ryuntech.saas.api.form.EmployeeForm;
import com.ryuntech.saas.api.form.ReceivableContractForm;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.model.FollowupRecord;
import com.ryuntech.saas.api.model.ReceivableContract;
import com.ryuntech.saas.api.service.ICustomerUserInfoService;
import com.ryuntech.saas.api.service.IEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;

/**
 * 小程序客户列表
 * @author EDZ
 */

@Slf4j
@RestController
@RequestMapping("/minicustomer")
@Api(value = "MiniCustomerController", tags = {"客户列表数据"})
public class MiniCustomerController extends ModuleBaseController{

    @Autowired
    private ICustomerUserInfoService customerUserInfoService;


    @Autowired
    private IEmployeeService iEmployeeService;

    @PostMapping("/outlist")
    @ApiOperation(value = "分页、条件查询客户列表信息")
    @ApiImplicitParam(name = "customerUserInfo", value = "查询条件", required = true, dataType = "CustomerUserInfo", paramType = "body")
    public Result<IPage<CustomerUserInfo>> list(@RequestBody CustomerUserInfoForm customerUserInfoForm, QueryPage queryPage) {
        log.info("customerUserInfoForm.getCustomerId()"+customerUserInfoForm.getCustomerId());
//        if (StringUtils.isBlank(customerUserInfo.getCustomerId())){
//            return new Result<>(OPERATE_ERROR);
//        }
        return  customerUserInfoService.selectPageList(customerUserInfoForm, queryPage);
    }

    /**
     * 添加合同跟进信息
     *
     * @param customerUserInfo
     * @return
     */
    @PostMapping("/outadd")
    @ApiOperation(value = "添加客户信息")
    @ApiImplicitParam(name = "customerUserInfo", value = "合同跟进信息", required = true, dataType = "CustomerUserInfo", paramType = "body")
    public Result add(@RequestBody CustomerUserInfo customerUserInfo) {
//            生成客户编号
        log.info("customerUserInfo.getCustomerId()"+customerUserInfo.getCustomerName());
        if (StringUtils.isBlank(customerUserInfo.getCustomerName())){
            return new Result<>(OPERATE_ERROR,"客户姓名为空");
        }
//        生成客户编号
        long id = generateId();
        String customerId = String.valueOf(id);
        customerUserInfo.setCustomerId(customerId);
        boolean b = customerUserInfoService.addCustomerByF(customerUserInfo);
        if (b){
            return new Result<>();
        }else {
            return new Result<>(OPERATE_ERROR);
        }
    }

    @PostMapping("/outedit")
    @ApiOperation(value = "修改合同信息")
    @ApiImplicitParam(name = "receivableContractFrom", value = "合同实体信息", required = true, dataType = "ReceivableContractFrom", paramType = "body")
    public Result edit(@RequestBody CustomerUserInfoForm receivableContractFrom) {
        if (StringUtils.isBlank(receivableContractFrom.getCustomerId())){
            return new Result<>(OPERATE_ERROR,"客户编号为空");
        }
        BaseForm baseForm = new BaseForm();
        baseForm.setAClass(CustomerUserInfoForm.class);
        baseForm.setT(receivableContractFrom);

        BaseDto baseDto = new BaseDto();
        baseDto.setAClass(CustomerUserInfo.class);
        CustomerUserInfo customerUserInfo = new CustomerUserInfo();
        baseDto.setT(customerUserInfo);

        CopyUtil.copyObject2(baseForm,baseDto);
//        开始更新
        boolean b = customerUserInfoService.updateById(customerUserInfo);
        if (b){
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }
    }

    /**
     * 根据ID查询用户信息
     *
     * @param customerUserInfo
     * @return
     */
    @GetMapping("/outFindById")
    @ApiOperation(value = "查询详细合同信息", notes = "customerId存在")
    @ApiImplicitParam(name = "customerId", value = "客户对象", required = true, dataType = "String")
    public Result<CustomerUserInfoDTO> findById(CustomerUserInfo customerUserInfo) {
        if (StringUtils.isBlank(customerUserInfo.getCustomerId())){
            return new Result<>(OPERATE_ERROR,"用户编号为空");
        }
        CustomerUserInfoDTO customerUserInfoDTO = customerUserInfoService.selectCustomerUserInfoDTO(customerUserInfo);
        return new Result<>(customerUserInfoDTO);
    }

    /**
     * 根据ID查询用户信息
     *
     * @param employeeForm
     * @return
     */
    @GetMapping("/outupdateyee")
    @ApiOperation(value = "更新员工信息", notes = "employeeId存在")
    @ApiImplicitParam(name = "employeeForm", value = "员工对象", required = true, dataType = "String")
    public Result findById(EmployeeForm employeeForm) {
        String employeeId = employeeForm.getEmployeeId();
        if (StringUtils.isBlank(employeeId)){
            return new Result<>(OPERATE_ERROR,"员工编号为空");
        }
        Employee employeeUp = iEmployeeService.selectByEmployee(new Employee().setEmployeeId(employeeId));
        if (null!=employeeUp){
            employeeUp.setEmail(employeeForm.getEmail());
            boolean b = iEmployeeService.updateById(employeeUp);
            if (b){
                return new Result("更新成功");
            }else {
                return new Result("更新失败");
            }
        }
        return new Result("更新成功");
    }


}
