package com.ryuntech.saas.outcontroller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.ReceivableContractDTO;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import com.ryuntech.saas.api.model.ReceivableContract;
import com.ryuntech.saas.api.model.SysUser;
import com.ryuntech.saas.api.service.ICustomerUserInfoService;
import com.ryuntech.saas.api.service.IReceivableContractService;
import com.ryuntech.saas.api.service.SysUserService;
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

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;
import static com.ryuntech.common.constant.enums.CommonEnums.PARAM_ERROR;

/**
 * @author EDZ
 */
@Slf4j
@RestController
@RequestMapping("/minicontract")
@Api(value = "MiniContractController", tags = {"应收合同数据"})
public class MiniContractController extends ModuleBaseController {


    @Autowired
    private IReceivableContractService iReceivableContractService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ICustomerUserInfoService iCustomerUserInfoService;

    @PostMapping("/outlist")
    @ApiOperation(value = "分页、条件查询用户列表信息")
    @ApiImplicitParam(name = "receivableContract", value = "查询条件", required = true, dataType = "ReceivableContract", paramType = "body")
    public Result<IPage<ReceivableContract>> list(@RequestBody ReceivableContract receivableContract, QueryPage queryPage) {
        log.info("receivableContract.getContractId()"+receivableContract.getContractId());
        return  iReceivableContractService.selectPageList(receivableContract, queryPage);
    }


    /**
     * 添加合同信息
     *
     * @param receivableContractDTO
     * @return
     */
    @PostMapping("/outadd")
    @ApiOperation(value = "添加合同信息")
    @ApiImplicitParam(name = "receivableContract", value = "合同实体信息", required = true, dataType = "ReceivableContract", paramType = "body")
    public Result add(@RequestBody ReceivableContractDTO receivableContractDTO) {

        if (StringUtils.isBlank(receivableContractDTO.getContractName())){
            return new Result(PARAM_ERROR,"合同名不能为空");
        }
//        if (StringUtils.isBlank(receivableContractDTO.getStaffId())) {
//            return new Result(PARAM_ERROR,"负责员工不能为空");
//        }
        ReceivableContract receivableContract = new ReceivableContract();
        receivableContract.setContractId(String.valueOf(generateId()));
        receivableContract.setBalanceAmount(receivableContractDTO.getContractAmount());
        receivableContract.setCollectionAmount("0.00");

        String staffId = receivableContractDTO.getStaffId();
        if (StringUtils.isNotBlank(staffId)){
            SysUser sysUser = sysUserService.getById(staffId);
            receivableContract.setStaffName(sysUser.getUsername());
        }
        String customerId = receivableContractDTO.getCustomerId();
        if (StringUtils.isNotBlank(customerId)){
            CustomerUserInfo customerUserInfo = iCustomerUserInfoService.getById(customerId);
            receivableContract.setCustomerName(customerUserInfo.getCustomerName());
        }
        receivableContract.setStatus("2");
//        receivableContract.setAttachmentCode()

        boolean b = iReceivableContractService.saveOrUpdate(receivableContract);
        if (b){
            //更新成功
            return new Result(receivableContract);
        }else {
            return new Result(OPERATE_ERROR);
        }

    }
}
