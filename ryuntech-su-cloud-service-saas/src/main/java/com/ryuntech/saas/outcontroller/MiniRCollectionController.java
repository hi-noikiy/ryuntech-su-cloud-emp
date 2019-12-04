package com.ryuntech.saas.outcontroller;


import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.form.ReceivableCollectionPlanForm;
import com.ryuntech.saas.api.form.ReceivableContractForm;
import com.ryuntech.saas.api.helper.constant.PlanConstant;
import com.ryuntech.saas.api.helper.constant.ReceivableContractConstants;
import com.ryuntech.saas.api.model.ReceivableCollection;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import com.ryuntech.saas.api.model.ReceivableContract;
import com.ryuntech.saas.api.service.IReceivableCollectionPlanService;
import com.ryuntech.saas.api.service.IReceivableCollectionService;
import com.ryuntech.saas.api.service.IReceivableContractService;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;

/**
 * @author EDZ
 */
@Slf4j
@RestController
@RequestMapping("/minicollection")
@Api(value = "MiniReceivableCollection", tags = {"小程序收款接口"})
public class MiniRCollectionController extends ModuleBaseController{


    @Autowired
    private IReceivableCollectionService iReceivableCollectionService;

    @Autowired
    private IReceivableCollectionPlanService iReceivableCollectionPlanService;

    @Autowired
    private IReceivableContractService iReceivableContractService;

    /**
     * 添加收款信息
     *
     * @param receivableCollection
     * @return
     */
    @PostMapping("/outadd")
    @ApiOperation(value = "添加收款信息")
    @ApiImplicitParam(name = "receivableCollection", value = "收款信息", required = true, dataType = "receivableCollection", paramType = "body")
    public Result add(@RequestBody ReceivableCollection receivableCollection) {
//            生成客户编号
        log.info("customerUserInfo.getCollectionId()"+receivableCollection.getCollectionId());
        if (StringUtils.isBlank(receivableCollection.getAmount())){
            return new Result<>(OPERATE_ERROR,"收款金额为空");
        }
        String contractId = receivableCollection.getContractId();
        if (StringUtils.isBlank(contractId)){
            return new Result<>(OPERATE_ERROR,"合同编号为空");
        }
//        生成收款编号
        String  collectionId = String.valueOf(generateId());
        receivableCollection.setCollectionId(collectionId);
//        收款金额
        String amount = receivableCollection.getAmount();
        BigDecimal amountDe=new BigDecimal(amount);
        receivableCollection.setCreateTime(new Date());
//        查询合同数据
        ReceivableContract byContract = iReceivableContractService.findByContract(new ReceivableContractForm().setContractId(contractId));
//        收款金额不能大于合同剩余金额
        if (amountDe.compareTo(new BigDecimal(byContract.getContractAmount()))==1){
            return new Result<>(OPERATE_ERROR,"收款金额不能大于合同剩余金额");
        }
//        客户编号
        receivableCollection.setCustomerId(byContract.getCustomerId());
//        客户名称
        receivableCollection.setCustomerName(byContract.getCustomerName());
//        合同编号
        receivableCollection.setContractId(byContract.getContractId());
//        查询回款计划 暂定为未开始
        ReceivableCollectionPlanForm receivableCollectionPlanForm = new ReceivableCollectionPlanForm();
        receivableCollectionPlanForm.setContractId(contractId);
        ArrayList<String> statusList = new ArrayList<>();
        statusList.add(PlanConstant.STARTING);
        statusList.add(PlanConstant.NOTSTARTED);
        receivableCollectionPlanForm.setStatusList(statusList);
//        如果当前还款金额还款金额大于计划金额，则计入下一期
            List<ReceivableCollectionPlan> rCollectionPlans = iReceivableCollectionPlanService.selectByPlan(receivableCollectionPlanForm);
            if (null==rCollectionPlans||rCollectionPlans.size()==0){
//                说明没有回款计划
//                设置状态还款状态
                //                    查询合同金额
                String contractAmount = byContract.getContractAmount();
                BigDecimal contractAmountDe=new BigDecimal(contractAmount);
                if (amountDe.compareTo(contractAmountDe)==-1){
//                    amountDe<contractAmountDe
//                    设置状态还款中
                    byContract.setStatus(ReceivableContractConstants.NOTSTARTED);
//                    设置已还款金额
                    byContract.setCollectionAmount(amountDe.toString());
//                    设置待还款金额
                    byContract.setBalanceAmount(contractAmountDe.subtract(amountDe).toString());
//                    插入一条还款记录，只有合同编号，没有计划
                    receivableCollection.setCreateBy(DateUtil.formatDate(new Date()));
                    receivableCollection.setTime(new Date());
                    iReceivableCollectionService.addReceivableCollection(null,receivableCollection,byContract);
                }
            }else {
                for (ReceivableCollectionPlan rPlan:rCollectionPlans){
                    if (amountDe.compareTo(new BigDecimal(0))<=0){
//                        当前回款金额已消耗完
                        break;
                    }
                    //        计划金额
                    String planAmount = rPlan.getPlanAmount();
                    BigDecimal planAmountDe=new BigDecimal(planAmount);
//                    查询待还款
                    String balanceAmount = byContract.getBalanceAmount();
                    BigDecimal balanceAmountDe=new BigDecimal(balanceAmount);
//                    查询已还款
                    String collectionAmount = byContract.getCollectionAmount();
                    BigDecimal collectionAmountDe=new BigDecimal(collectionAmount);

                    if (amountDe.compareTo(planAmountDe)==-1){
//            amountDe<planAmountDe  收款金额小于当前计划还款金额
//            设置回款计划状态为回款中
                        rPlan.setStatus(PlanConstant.STARTING);
//            设置已回金额
                        rPlan.setBackedAmount(amountDe.toString());
//            设置未回金额
                        rPlan.setSurplusAmount(planAmountDe.subtract(amountDe).toString());
                    }
//         如果当前还款金额还款金额等于计划金额，修改状态为已回款
                    if (amountDe.compareTo(planAmountDe)>=0){
                        //            设置回款计划状态为已回款
                        rPlan.setStatus(PlanConstant.REIMBURSEMENT);
                        rPlan.setBackedAmount(planAmountDe.toString());
                        rPlan.setSurplusAmount("0.00");
                    }
//                        设置合同待还金额
                    BigDecimal balanceAmountDe2 = balanceAmountDe.subtract(amountDe);
                    byContract.setBalanceAmount(balanceAmountDe2.toString());
//                        设置合同已还金额
                    BigDecimal addDe = collectionAmountDe.add(amountDe);
                    byContract.setCollectionAmount(addDe.toString());
                    amountDe = planAmountDe.subtract(amountDe);
                }
                receivableCollection.setCreateBy(DateUtil.formatDate(new Date()));
                receivableCollection.setTime(new Date());
                iReceivableCollectionService.addReceivableCollection(rCollectionPlans,receivableCollection,byContract);
            }

        return new Result<>();
    }

}
