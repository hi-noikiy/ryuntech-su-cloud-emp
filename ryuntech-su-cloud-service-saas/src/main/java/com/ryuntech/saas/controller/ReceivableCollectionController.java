package com.ryuntech.saas.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.model.BaseDto;
import com.ryuntech.common.model.BaseForm;
import com.ryuntech.common.utils.CopyUtil;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.form.ReceivableCollectionConditionForm;
import com.ryuntech.saas.api.form.ReceivableCollectionForm;
import com.ryuntech.saas.api.helper.constant.PlanConstant;
import com.ryuntech.saas.api.helper.constant.ReceivableContractConstants;
import com.ryuntech.saas.api.model.ReceivableCollection;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import com.ryuntech.saas.api.model.ReceivableContract;
import com.ryuntech.saas.api.service.IReceivableCollectionPlanService;
import com.ryuntech.saas.api.service.IReceivableCollectionService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;

/**
 * <p>
 * 回款表 前端控制器
 * </p>
 *
 * @author antu
 * @since 2019-09-27
 */
@RestController
@RequestMapping("/collection")
public class ReceivableCollectionController extends ModuleBaseController {
    @Autowired
    private IReceivableCollectionService iReceivableCollectionService;

    @Autowired
    private IReceivableCollectionPlanService iReceivableCollectionPlanService;

    /**
     * 删除回款信息
     *
     * @param collectionId
     * @return
     */
    @DeleteMapping("/{collectionId}")
    @ApiOperation(value = "删除回款")
    @ApiImplicitParam(name = "collectionId", value = "回款编号", required = true, dataType = "String")
    public Result delete(@PathVariable String collectionId) {
        boolean isSuccess =iReceivableCollectionService.removeById(collectionId);
        if (isSuccess){
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }
    }

    /**
     * 回款信息作废
     *
     * @param collectionId
     * @return
     */
    @PostMapping("/collectionzuofei/{collectionId}")
    @ApiOperation(value = "回款作废")
    @ApiImplicitParam(name = "collectionId", value = "回款作废编号", required = true, dataType = "String")
    public Result zuofei(@PathVariable String collectionId) {
        ReceivableCollection receivableCollection = new ReceivableCollection();
        receivableCollection.setCollectionId(collectionId);
        receivableCollection.setStatus("0");
        boolean isSuccess =iReceivableCollectionService.updateById(receivableCollection);
        if (isSuccess){
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }
    }

    /**
     * 分页查询列表数据，条件查询
     *
     * @param receivableCollectionConditionForm
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页、条件查询回款列表信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "receivableCollection", value = "查询条件", dataType = "ReceivableCollection", paramType = "body"),
            @ApiImplicitParam(name="queryPage",value="分页信息",dataType="QueryPage", paramType = "body")
    })
    public Result<IPage<ReceivableCollection>> list(@RequestBody ReceivableCollectionConditionForm receivableCollectionConditionForm, QueryPage queryPage) {
        return iReceivableCollectionService.selectPageList(receivableCollectionConditionForm,queryPage);
    }

    /**
     * 添加回款信息
     *
     * @param receivableCollectionForm
     * @return
     */
    @PostMapping("/collectionadd")
    @ApiOperation(value = "添加回款信息")
    @ApiImplicitParam(name = "receivableCollection", value = "回款实体信息", required = true, dataType = "ReceivableCollection", paramType = "body")
    public Result add(@RequestBody ReceivableCollectionForm receivableCollectionForm) {
        receivableCollectionForm.setCollectionId(String.valueOf(generateId()));
        receivableCollectionForm.setCreateBy(getUserName());
//        ReceivableCollectionPlan receivableCollectionPlan = new ReceivableCollectionPlan();
        ReceivableContract receivableContract = new ReceivableContract();
        ReceivableCollection receivableCollection = new ReceivableCollection();
        // 将receivableCollectionForm相应字段数据copy到receivableCollection中
        BaseForm baseForm = new BaseForm();
        baseForm.setAClass(ReceivableCollectionForm.class);
        baseForm.setT(receivableCollectionForm);
        BaseDto baseDto = new BaseDto();
        baseDto.setAClass(ReceivableCollection.class);
        baseDto.setT(receivableCollection);
        CopyUtil.copyObject2(baseForm, baseDto);
        receivableCollection.setCreateTime(new Date());
        receivableContract.setContractId(receivableCollectionForm.getContractId());
        BigDecimal balAmount = new BigDecimal(receivableCollectionForm.getBalanceAmount());
        BigDecimal collAmount = new BigDecimal(receivableCollectionForm.getCollectionAmount());
        BigDecimal amount = new BigDecimal(receivableCollectionForm.getAmount());
        BigDecimal balanceAmount = balAmount.subtract(amount);
        BigDecimal collectionAmount = collAmount.add(amount);
        receivableContract.setContractId(receivableCollectionForm.getContractId());
        receivableContract.setBalanceAmount(balanceAmount.toString());
        receivableContract.setCollectionAmount(collectionAmount.toString());
        if(0 == Double.parseDouble(receivableContract.getBalanceAmount())) {
            receivableContract.setStatus(ReceivableContractConstants.REIMBURSEMENT);
        }

        // 查询合同还款计划
        QueryWrapper<ReceivableCollectionPlan> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("contract_id", receivableCollectionForm.getContractId()).orderByAsc("plan_time");
        List<ReceivableCollectionPlan> receivableCollectionPlans = iReceivableCollectionPlanService.list(queryWrapper);
        List<ReceivableCollectionPlan> receivableCollectionPlans1 = new ArrayList<>();
        // 如果有还款计划，则将回款金额分摊
        if(receivableCollectionPlans != null && receivableCollectionPlans.size() != 0) {
            double repayAmount = Double.parseDouble(receivableCollectionForm.getAmount());
            for(ReceivableCollectionPlan receivableCollectionPl : receivableCollectionPlans) {
                double surplusAmount = Double.parseDouble(receivableCollectionPl.getSurplusAmount());
                if(surplusAmount > 0) {
                    if(surplusAmount >= repayAmount) {
                        BigDecimal surplusAmount1 = new BigDecimal(surplusAmount).subtract(new BigDecimal(repayAmount));
                        receivableCollectionPl.setSurplusAmount(surplusAmount1.toString());
                        BigDecimal backedAmount = new BigDecimal(receivableCollectionPl.getBackedAmount()).add(new BigDecimal(repayAmount));
                        receivableCollectionPl.setBackedAmount(backedAmount.toString());
                        if(surplusAmount1.doubleValue() == 0) {
                            receivableCollectionPl.setStatus(PlanConstant.REIMBURSEMENT);
                        } else if(receivableCollectionPl.getPlanTime().before(new Date())) {
                            receivableCollectionPl.setStatus(PlanConstant.OVERDUED);
                        }
                    } else {
                        repayAmount = new BigDecimal(repayAmount).subtract(new BigDecimal(receivableCollectionPl.getSurplusAmount())).doubleValue();
                        receivableCollectionPl.setBackedAmount(receivableCollectionPl.getPlanAmount());
                        receivableCollectionPl.setSurplusAmount("0");
                        receivableCollectionPl.setStatus(PlanConstant.REIMBURSEMENT);
                    }
                }
                receivableCollectionPlans1.add(receivableCollectionPl);
            }
        }

        boolean b = iReceivableCollectionService.addReceivableCollection(receivableCollectionPlans1, receivableCollection, receivableContract);
        if(b) {
            return new Result();
        } else {
            return new Result(OPERATE_ERROR);
        }

    }

    /**
     * 根据合同编号查询回款记录
     *
     * @param contractId
     * @return
     */
    @PostMapping("/listByContractId/{contractId}")
    @ApiOperation(value = "根据合同编号查询回款记录")
    @ApiImplicitParam(name = "receivableCollection", value = "回款实体信息", required = true, dataType = "String")
    public Result listByContractId(@PathVariable String contractId) {
        QueryWrapper<ReceivableCollection> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("contract_id", contractId);
        List<ReceivableCollection> receivableCollectionList = iReceivableCollectionService.list(queryWrapper);
        if(receivableCollectionList == null) {
            return new Result();
        } else {
            return new Result(receivableCollectionList);
        }

    }

}
