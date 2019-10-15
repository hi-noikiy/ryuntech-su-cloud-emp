package com.ryuntech.saas.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import com.ryuntech.saas.api.model.ReceivableContract;
import com.ryuntech.saas.api.model.SysUser;
import com.ryuntech.saas.api.service.ICustomerUserInfoService;
import com.ryuntech.saas.api.service.IReceivableContractService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;
import static com.ryuntech.common.constant.enums.CommonEnums.PARAM_ERROR;

/**
 * <p>
 * 客户信息表 前端控制器
 * </p>
 *
 * @author antu
 * @since 2019-09-29
 */
@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerUserInfoController extends ModuleBaseController {
    @Autowired
    private ICustomerUserInfoService iCustomerUserInfoService;

    /**
     * 分页查询列表数据，条件查询
     *
     * @param customerUserInfo
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页、条件查询客户列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "customerUserInfo", value = "查询条件", dataType = "CustomerUserInfo", paramType = "body"),
            @ApiImplicitParam(name="queryPage",value="分页信息",dataType="QueryPage", paramType = "body")
    })
    public Result<IPage<CustomerUserInfo>> list(@RequestBody CustomerUserInfo customerUserInfo, QueryPage queryPage) {
        return iCustomerUserInfoService.selectPageList(customerUserInfo,queryPage);
    }



    /**
     * 添加客户信息
     *
     * @param customerUserInfo
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加客户")
    @ApiImplicitParam(name = "customerUserInfo", value = "客户实体信息", required = true, dataType = "CustomerUserInfo", paramType = "body")
    public Result add(@RequestBody CustomerUserInfo customerUserInfo) {

        if (StringUtils.isBlank(customerUserInfo.getCustomerName())){
            return new Result(PARAM_ERROR,"客户名不能为空");
        }
        if (StringUtils.isBlank(customerUserInfo.getContacts())){
            return new Result(PARAM_ERROR,"联系人不能为空");
        }
        if (customerUserInfo.getContactsPhone()==null){
            return new Result(PARAM_ERROR,"联系人手机不能为空");
        }
        customerUserInfo.setCustomerId(String.valueOf(generateId()));
        boolean b = iCustomerUserInfoService.saveOrUpdate(customerUserInfo);
        if (b){
            //更新成功
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }
    }


    /**
     * 更新客户信息
     *
     * @param customerUserInfo
     * @return
     */
    @PutMapping("/edit")
    @ApiOperation(value = "更新客户")
    @ApiImplicitParam(name = "customerUserInfo", value = "客户信息", required = true, dataType = "CustomerUserInfo", paramType = "body")
    public Result edit(@RequestBody CustomerUserInfo customerUserInfo) {
        if (StringUtils.isBlank(customerUserInfo.getCustomerId())){
            return new Result(PARAM_ERROR,"客户不能为空");
        }
        if (StringUtils.isBlank(customerUserInfo.getCustomerName())){
            return new Result(PARAM_ERROR,"联系人不能为空");
        }
        if (StringUtils.isBlank(customerUserInfo.getContactsPhone())){
            return new Result(PARAM_ERROR,"联系电话不能为空");
        }
        boolean b = iCustomerUserInfoService.saveOrUpdate(customerUserInfo);
        if (b){
            //更新成功
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }
    }

    /**
     * 根据ID查询用户信息
     *
     * @param customerId
     * @return
     */
    @GetMapping("/{customerId}")
    @ApiOperation(value = "查询详细客户信息", notes = "customerId存在且大于0")
    @ApiImplicitParam(name = "customerId", value = "用户编号", required = true, dataType = "String")
    public Result<CustomerUserInfo> findById(@PathVariable String customerId) {
        if (StringUtils.isBlank(customerId)) {
            return new Result<>();
        } else {
            return new Result<>(iCustomerUserInfoService.getById(customerId));
        }
    }


    /**
     * 删除用户信息
     *
     * @param customerId
     * @return
     */
    @DeleteMapping("/{customerId}")
    @ApiOperation(value = "删除客户")
    @ApiImplicitParam(name = "customerId", value = "客户编号", required = true, dataType = "String")
    public Result delete(@PathVariable String customerId) {
        boolean isSuccess = iCustomerUserInfoService.removeById(customerId);
        if (isSuccess){
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }
    }

    /**
     * 获取用户id和用户名列表，用于搜索选择
     * @param customerUserInfo
     * @return
     */
    @PostMapping("/getCustomerMap")
    public Result selectUserMap(@RequestBody CustomerUserInfo customerUserInfo) {
        List<CustomerUserInfo> list = iCustomerUserInfoService.selectCustomerMap(customerUserInfo);
        List<Map<String,String>> res = new ArrayList<>();
        for (CustomerUserInfo customerUserInfo1 : list) {
            Map<String,String> item = new HashMap<>();
            item.put("customer_id",customerUserInfo1.getCustomerId());
            item.put("customer_name",customerUserInfo1.getCustomerName());
            res.add(item);
        }
        return new Result(res);
    }

}
