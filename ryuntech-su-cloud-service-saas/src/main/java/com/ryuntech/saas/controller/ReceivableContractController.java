package com.ryuntech.saas.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.Order;
import com.ryuntech.saas.api.model.PaymentResult;
import com.ryuntech.saas.api.model.ReceivableContract;
import com.ryuntech.saas.api.model.SysUser;
import com.ryuntech.saas.api.service.IPartnerService;
import com.ryuntech.saas.api.service.IReceivableContractService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@RestController
@RequestMapping("/contract")
public class ReceivableContractController extends ModuleBaseController {

    @Autowired
    private IReceivableContractService iReceivableContractService;

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
    public Result<IPage<ReceivableContract>> list(@RequestBody ReceivableContract receivableContract, QueryPage queryPage) {
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
        if (receivableContract.getUrl()==null){
            return new Result(PARAM_ERROR,"合同附件不能为空");
        }
        receivableContract.setContractId(String.valueOf(generateId()));
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
        if (StringUtils.isBlank(receivableContract.getCollectionId())){
            return new Result(PARAM_ERROR,"合同编号不能为空");
        }
        if (StringUtils.isBlank(receivableContract.getContractName())){
            return new Result(PARAM_ERROR,"合同名不能为空");
        }
        if (StringUtils.isBlank(receivableContract.getUrl())){
            return new Result(PARAM_ERROR,"合同附件不能为空");
        }
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


}
