package com.ryuntech.saas.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.ReceivableContractDTO;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.ICustomerUserInfoService;
import com.ryuntech.saas.api.service.IPartnerService;
import com.ryuntech.saas.api.service.IReceivableContractService;
import com.ryuntech.saas.api.service.SysUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;
import static com.ryuntech.common.constant.enums.CommonEnums.PARAM_ERROR;

/**
 * <p>
 * 应收合同表 前端控制器
 * </p>
 *
 * @author antu
 * @since 2019-09-27
 */
@Slf4j
@RestController
@RequestMapping("/contract")
public class ReceivableContractController extends ModuleBaseController {

    @Autowired
    private IReceivableContractService iReceivableContractService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ICustomerUserInfoService iCustomerUserInfoService;

    /**
     * 分页查询列表数据，条件查询
     *
     * @param receivableContract
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页、条件查询合同列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receivableContract", value = "查询条件", dataType = "ReceivableContract", paramType = "body"),
            @ApiImplicitParam(name="queryPage",value="分页信息",dataType="QueryPage", paramType = "body")
    })
    public Result<IPage<ReceivableContractDTO>> list(@RequestBody ReceivableContract receivableContract, QueryPage queryPage) {
        return iReceivableContractService.selectPageList(receivableContract,queryPage);
    }



    /**
     * 添加合同信息
     *
     * @param receivableContract
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加合同信息")
    @ApiImplicitParam(name = "receivableContract", value = "合同实体信息", required = true, dataType = "ReceivableContract", paramType = "body")
    public Result add(@RequestBody ReceivableContract receivableContract) {

        if (receivableContract.getContractName()==null){
            return new Result(PARAM_ERROR,"合同名不能为空");
        }
        if (StringUtils.isBlank(receivableContract.getStaffId())) {
            return new Result(PARAM_ERROR,"负责员工不能为空");
        }

        receivableContract.setContractId(String.valueOf(generateId()));
        receivableContract.setBalanceAmount(receivableContract.getContractAmount());
        receivableContract.setCollectionAmount("0");

        String staffId = receivableContract.getStaffId();
        SysUser sysUser = sysUserService.getById(staffId);
        receivableContract.setStaffName(sysUser.getUsername());

        String customerId = receivableContract.getCustomerId();
        CustomerUserInfo customerUserInfo = iCustomerUserInfoService.getById(customerId);
        receivableContract.setCustomerName(customerUserInfo.getCustomerName());

        receivableContract.setStatus("2");

        boolean b = iReceivableContractService.saveOrUpdate(receivableContract);
        if (b){
            //更新成功
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }
    }


    /**
     * 更新订单信息
     *
     * @param receivableContract
     * @return
     */
    @PutMapping("/edit")
    @ApiOperation(value = "更新合同")
    @ApiImplicitParam(name = "receivableContract", value = "合同信息", required = true, dataType = "ReceivableContract", paramType = "body")
    public Result edit(@RequestBody ReceivableContract receivableContract) {
        if (StringUtils.isBlank(receivableContract.getContractId())){
            return new Result(PARAM_ERROR,"合同编号不能为空");
        }
        if (StringUtils.isBlank(receivableContract.getContractName())){
            return new Result(PARAM_ERROR,"合同名不能为空");
        }
        if (StringUtils.isBlank(receivableContract.getCustomerId())) {
            return new Result(PARAM_ERROR,"签约客户不能为空");
        }
        if (StringUtils.isBlank(receivableContract.getStaffId())) {
            return new Result(PARAM_ERROR,"负责员工不能为空");
        }
        if (StringUtils.isBlank(receivableContract.getAttachmentCode())){
            return new Result(PARAM_ERROR,"合同附件不能为空");
        }

        String staffId = receivableContract.getStaffId();
        SysUser sysUser = sysUserService.getById(staffId);
        receivableContract.setStaffName(sysUser.getUsername());

        String customerId = receivableContract.getCustomerId();
        CustomerUserInfo customerUserInfo = iCustomerUserInfoService.getById(customerId);
        receivableContract.setCustomerName(customerUserInfo.getCustomerName());

        boolean b = iReceivableContractService.saveOrUpdate(receivableContract);
        if (b){
            //更新成功
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }
    }

    /**
     * 删除用户信息
     *
     * @param contractId
     * @return
     */
    @DeleteMapping("/{contractId}")
    @ApiOperation(value = "删除合同")
    @ApiImplicitParam(name = "contractId", value = "合同编号", required = true, dataType = "Long")
    public Result delete(@PathVariable Long contractId) {
        iReceivableContractService.removeById(contractId);
        return new Result();
    }

    /**
     * 根据合同ID获取合同详情
     * @param contractId
     * @return
     */
    @GetMapping("/{contractId}")
    public Result get(@PathVariable String contractId) {
        if (StringUtils.isBlank(contractId)){
            return new Result(CommonEnums.PARAM_ERROR,"参数错误");
        }
        ReceivableContract contract_info = iReceivableContractService.getById(contractId);
        return new Result(contract_info);
    }

}
