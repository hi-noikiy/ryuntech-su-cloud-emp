package com.ryuntech.saas.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.ReceivableCollectionPlanDTO;
import com.ryuntech.saas.api.dto.ReceivableContractDTO;
import com.ryuntech.saas.api.form.ReceivableContractForm;
import com.ryuntech.saas.api.helper.constant.AttachmentConstants;
import com.ryuntech.saas.api.helper.constant.PlanConstant;
import com.ryuntech.saas.api.helper.constant.ReceivableContractConstants;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.ICustomerUserInfoService;
import com.ryuntech.saas.api.service.IReceivableContractService;
import com.ryuntech.saas.api.service.SysUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

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
    public Result<IPage<ReceivableContract>> list(@RequestBody ReceivableContract receivableContract, QueryPage queryPage) {
        return iReceivableContractService.pageList(receivableContract,queryPage);
    }

    /**
     * 添加合同信息
     *
     * @param receivableContractFrom
     * @return
     */
    @PostMapping("/contractadd")
    @ApiOperation(value = "添加合同信息")
    @ApiImplicitParam(name = "receivableContractFrom", value = "合同实体信息", required = true, dataType = "ReceivableContractFrom", paramType = "body")
    public Result add(@RequestBody ReceivableContractForm receivableContractFrom) {

        if (StringUtils.isBlank(receivableContractFrom.getContractName())){
            return new Result(PARAM_ERROR,"合同名不能为空");
        }
//        if (StringUtils.isBlank(receivableContractDTO.getStaffId())) {
//            return new Result(PARAM_ERROR,"负责员工不能为空");
//        }
        ReceivableContract receivableContract = new ReceivableContract();
//        合同编号
        String contractId = String.valueOf(generateId());
        receivableContract.setContractId(contractId);
//        合同名称
        receivableContract.setContractName(receivableContractFrom.getContractName());
        //        合同编码
        String contractCode = String.valueOf(generateId());
        receivableContract.setContractCode(contractCode);
        //        合同金额
        receivableContract.setContractAmount(receivableContractFrom.getContractAmount());
        // 合同日期
        receivableContract.setContractTime(receivableContractFrom.getContractTime());
//        已回款金额
        receivableContract.setCollectionAmount("0.00");
        //待回款金额
        receivableContract.setBalanceAmount(receivableContract.getContractAmount());
//        合同状态
        receivableContract.setStatus(ReceivableContractConstants.NOTSTARTED);
//        //附件编码
//        receivableContract.setAttachmentCode(receivableContractFrom.getAttachmentCode());
        //客户编号
        receivableContract.setCustomerId(receivableContractFrom.getCustomerId());
        //客户名称
        receivableContract.setCustomerName(receivableContractFrom.getCustomerName());
        //联系人
        receivableContract.setContacts(receivableContractFrom.getContacts());
        //联系人电话
        receivableContract.setContactsPhone(receivableContractFrom.getContactsPhone());
//       负责人编号
        receivableContract.setStaffId(receivableContractFrom.getStaffId());
        //负责人姓名
        receivableContract.setStaffName(receivableContractFrom.getStaffName());
        //部门
        receivableContract.setDepartment(receivableContractFrom.getDepartment());
//        //负责人编号
//        String staffId = receivableContractFrom.getStaffId();
//        if (StringUtils.isNotBlank(staffId)){
//            SysUser sysUser = sysUserService.getById(staffId);
//            receivableContract.setStaffName(sysUser.getUsername());
//        }
//        //       客户编号
//        String customerId = receivableContractFrom.getCustomerId();
//        if (StringUtils.isNotBlank(customerId)){
//            CustomerUserInfo customerUserInfo = iCustomerUserInfoService.getById(customerId);
//            receivableContract.setCustomerName(customerUserInfo.getCustomerName());
//        }

        List<ReceivableCollectionPlan> receivableCollectionPlans = new ArrayList<>();
        List<ReceivableCollectionPlanDTO> receivableCollectionPlanDTOs = receivableContractFrom.getReceivableCollectionPlanDTOs();
        if (receivableCollectionPlanDTOs!=null&&receivableCollectionPlanDTOs.size()!=0){
            for (ReceivableCollectionPlanDTO receivableCollectionPlanDTO :receivableCollectionPlanDTOs){
                ReceivableCollectionPlan receivableCollectionPlan = new ReceivableCollectionPlan();
                receivableCollectionPlan.setContractId(contractId);
//                计划编号
                String planId = String.valueOf(generateId());
                receivableCollectionPlan.setPlanId(planId);
                receivableCollectionPlan.setPlanAmount(receivableCollectionPlanDTO.getPlanAmount());
                receivableCollectionPlan.setBackedAmount("0");
                receivableCollectionPlan.setSurplusAmount(receivableCollectionPlan.getPlanAmount());
                receivableCollectionPlan.setStatus(PlanConstant.NOTSTARTED);
                receivableCollectionPlan.setRemakes(receivableCollectionPlanDTO.getRemakes());
                receivableCollectionPlan.setPlanTime(DateUtil.parseDate(receivableCollectionPlanDTO.getPlanTime()));
                receivableCollectionPlans.add(receivableCollectionPlan);
            }
        }

        List<AttachmentFile> attachmentFiles = receivableContractFrom.getFiles();
        List<Attachment> attachments = new ArrayList<>();
        if (attachmentFiles!=null&&attachmentFiles.size()!=0){
            //                附件编码
            String attachmentCode = String.valueOf(generateId());
            receivableContract.setAttachmentCode(attachmentCode);
            for (AttachmentFile attachmentFile :attachmentFiles){
                //                文件编号
                String attachmentId = String.valueOf(generateId());
                Attachment attachment = new Attachment();
                attachment.setId(attachmentId);
                attachment.setAttachmentType(AttachmentConstants.TYPE1);
                attachment.setAttachmentCode(attachmentCode);
                attachment.setAttachmentUrl(attachmentFile.getUrl());
                attachment.setStatus(AttachmentConstants.YES);
                attachment.setUpdatedAt(new Date());
                attachment.setCreatedAt(new Date());
                attachments.add(attachment);
            }
        }

        Boolean aBoolean = iReceivableContractService.addReceivableContract(attachments,receivableContract, receivableCollectionPlans);
        if (aBoolean){
            //更新成功
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }

    }

    /**
     * 修改合同信息
     *
     * @param receivableContractFrom
     * @return
     */
    @PostMapping("/contractupdate")
    @ApiOperation(value = "修改合同信息")
    @ApiImplicitParam(name = "receivableContractFrom", value = "合同实体信息", required = true, dataType = "ReceivableContractFrom", paramType = "body")
    public Result update(@RequestBody ReceivableContractForm receivableContractFrom) {
        if (StringUtils.isBlank(receivableContractFrom.getContractId())){
            return new Result(PARAM_ERROR,"合同编号不能为空");
        }
        ReceivableContract receivableContract = new ReceivableContract();
//        合同编号
        String contractId = receivableContractFrom.getContractId();
        receivableContract.setContractId(contractId);
//        合同名称
        receivableContract.setContractName(receivableContractFrom.getContractName());
//        合同总额
        receivableContract.setContractAmount(receivableContractFrom.getContractAmount());
        //        待还款金额
        receivableContract.setBalanceAmount(receivableContractFrom.getBalanceAmount());
//        已回款余额
        receivableContract.setCollectionAmount(receivableContractFrom.getCollectionAmount());
//        合同时间
        receivableContract.setContractTime(receivableContractFrom.getContractTime());
//        联系人
        receivableContract.setContacts(receivableContractFrom.getContacts());
        //        联系人电话
        receivableContract.setContactsPhone(receivableContractFrom.getContactsPhone());
//        合同编码
        receivableContract.setContractCode(receivableContractFrom.getContractCode());
        //       负责人编号
        receivableContract.setStaffId(receivableContractFrom.getStaffId());
        //        负责人姓名
        receivableContract.setStaffName(receivableContractFrom.getStaffName());

        String customerId = receivableContractFrom.getCustomerId();
        if (StringUtils.isNotBlank(customerId)){
            CustomerUserInfo customerUserInfo = iCustomerUserInfoService.getById(customerId);
            receivableContract.setCustomerName(customerUserInfo.getCustomerName());
            receivableContract.setCustomerId(customerId);
        }

        //        合同状态
        receivableContract.setStatus(ReceivableContractConstants.NOTSTARTED);

        List<ReceivableCollectionPlan> receivableCollectionPlans = new ArrayList<>();
        List<ReceivableCollectionPlanDTO> receivableCollectionPlanDTOs = receivableContractFrom.getReceivableCollectionPlanDTOs();
        if (receivableCollectionPlanDTOs!=null&&receivableCollectionPlanDTOs.size()!=0){
            for (ReceivableCollectionPlanDTO receivableCollectionPlanDTO :receivableCollectionPlanDTOs){
                ReceivableCollectionPlan receivableCollectionPlan = new ReceivableCollectionPlan();
                receivableCollectionPlan.setContractId(contractId);
//                计划编号， 判断当前计划编码是否存在
                String planIdDT = receivableCollectionPlanDTO.getPlanId();
                if (StringUtils.isBlank(planIdDT)){
                    planIdDT = String.valueOf(generateId());
                    receivableCollectionPlan.setBackedAmount("0");
                    receivableCollectionPlan.setSurplusAmount(receivableCollectionPlanDTO.getPlanAmount());
                    receivableCollectionPlan.setStatus(PlanConstant.NOTSTARTED);
                } else {
                    BigDecimal planAmount = new BigDecimal(receivableCollectionPlanDTO.getPlanAmount());
                    BigDecimal backedAmount = new BigDecimal(receivableCollectionPlanDTO.getBackAmount());
                    BigDecimal surplusAmount = new BigDecimal(receivableCollectionPlanDTO.getSurplusAmount());
                    BigDecimal planAmount1 = backedAmount.add(surplusAmount);
                    if (planAmount.compareTo(planAmount1) > 0) {
                        BigDecimal diffAmount = planAmount.subtract(planAmount1);
                        receivableCollectionPlan.setSurplusAmount(surplusAmount.add(diffAmount).toString());
                    } else if (planAmount.compareTo(planAmount1) < 0){
                        BigDecimal diffAmount = planAmount1.subtract(planAmount);
                        if (receivableCollectionPlanDTO.getStatus().equals("1")) {
                            receivableCollectionPlan.setBackedAmount(backedAmount.subtract(diffAmount).toString());
                        } else {
                            receivableCollectionPlan.setSurplusAmount(surplusAmount.subtract(diffAmount).toString());
                        }
                    } else {
                        receivableCollectionPlan.setBackedAmount(receivableCollectionPlanDTO.getBackAmount());
                        receivableCollectionPlan.setSurplusAmount(receivableCollectionPlanDTO.getSurplusAmount());
                    }
                }
                receivableCollectionPlan.setPlanId(planIdDT);
                receivableCollectionPlan.setPlanAmount(receivableCollectionPlanDTO.getPlanAmount());
                receivableCollectionPlan.setRemakes(receivableCollectionPlanDTO.getRemakes());
                receivableCollectionPlan.setPlanTime(DateUtil.parseDate(receivableCollectionPlanDTO.getPlanTime()));
                // 计划状态
                boolean b = receivableCollectionPlan.getPlanTime().before(new Date());
                double surplusAmount = Double.parseDouble(receivableCollectionPlan.getSurplusAmount());
                if (b) {
                   if (surplusAmount > 0) {
                       receivableCollectionPlan.setStatus(PlanConstant.OVERDUED);
                   } else if (surplusAmount == 0) {
                       receivableCollectionPlan.setStatus(PlanConstant.REIMBURSEMENT);
                   }
                }
                receivableCollectionPlans.add(receivableCollectionPlan);
                // 合同状态
                if(receivableCollectionPlan.getStatus().equals("0")) {
                    receivableContract.setStatus(ReceivableContractConstants.OVERDUED);
                }
            }
        }

        List<AttachmentFile> attachmentFiles = receivableContractFrom.getFiles();
        List<Attachment> attachments = new ArrayList<>();
        if (attachmentFiles!=null&&attachmentFiles.size()!=0){
            //                附件编码
            String attachmentCode = String.valueOf(generateId());
            receivableContract.setAttachmentCode(attachmentCode);
            for (AttachmentFile attachmentFile :attachmentFiles){
                //                文件编号
                Attachment attachment = new Attachment();
                String attachmentId=null;
                if (StringUtils.isBlank(attachmentFile.getId())){
                    attachmentId= String.valueOf(generateId());
                }
                attachment.setId(attachmentId);
                attachment.setAttachmentType(AttachmentConstants.TYPE1);
                attachment.setAttachmentCode(attachmentCode);
                attachment.setAttachmentUrl(attachmentFile.getUrl());
                attachment.setStatus(AttachmentConstants.YES);
                attachment.setUpdatedAt(new Date());
                attachment.setCreatedAt(new Date());
                attachments.add(attachment);
            }
        }

        Boolean aBoolean = iReceivableContractService.editReceivableContract(attachments,receivableContract, receivableCollectionPlans);
        if (aBoolean){
            //更新成功
            return new Result();
        }else {
            return new Result(OPERATE_ERROR);
        }
    }

//    /**
//     * 添加合同信息
//     *
//     * @param receivableContract
//     * @return
//     */
//    @PostMapping
//    @ApiOperation(value = "添加合同信息")
//    @ApiImplicitParam(name = "receivableContract", value = "合同实体信息", required = true, dataType = "ReceivableContract", paramType = "body")
//    public Result add(@RequestBody ReceivableContract receivableContract) {
//
//        if (receivableContract.getContractName()==null){
//            return new Result(PARAM_ERROR,"合同名不能为空");
//        }
//        if (StringUtils.isBlank(receivableContract.getStaffId())) {
//            return new Result(PARAM_ERROR,"负责员工不能为空");
//        }
//
//        receivableContract.setContractId(String.valueOf(generateId()));
//        receivableContract.setBalanceAmount(receivableContract.getContractAmount());
//        receivableContract.setCollectionAmount("0");
//
//        String staffId = receivableContract.getStaffId();
//        SysUser sysUser = sysUserService.getById(staffId);
//        receivableContract.setStaffName(sysUser.getUsername());
//
//        String customerId = receivableContract.getCustomerId();
//        CustomerUserInfo customerUserInfo = iCustomerUserInfoService.getById(customerId);
//        receivableContract.setCustomerName(customerUserInfo.getCustomerName());
//
//        receivableContract.setStatus("2");
//
//        boolean b = iReceivableContractService.saveOrUpdate(receivableContract);
//        if (b){
//            //更新成功
//            return new Result();
//        }else {
//            return new Result(OPERATE_ERROR);
//        }
//    }


//    /**
//     * 更新订单信息
//     *
//     * @param receivableContract
//     * @return
//     */
//    @PutMapping("/edit")
//    @ApiOperation(value = "更新合同")
//    @ApiImplicitParam(name = "receivableContract", value = "合同信息", required = true, dataType = "ReceivableContract", paramType = "body")
//    public Result edit(@RequestBody ReceivableContract receivableContract) {
//        if (StringUtils.isBlank(receivableContract.getContractId())){
//            return new Result(PARAM_ERROR,"合同编号不能为空");
//        }
//        if (StringUtils.isBlank(receivableContract.getContractName())){
//            return new Result(PARAM_ERROR,"合同名不能为空");
//        }
//        if (StringUtils.isBlank(receivableContract.getCustomerId())) {
//            return new Result(PARAM_ERROR,"签约客户不能为空");
//        }
//        if (StringUtils.isBlank(receivableContract.getStaffId())) {
//            return new Result(PARAM_ERROR,"负责员工不能为空");
//        }
//        if (StringUtils.isBlank(receivableContract.getAttachmentCode())){
//            return new Result(PARAM_ERROR,"合同附件不能为空");
//        }
//
//        String staffId = receivableContract.getStaffId();
//        SysUser sysUser = sysUserService.getById(staffId);
//        receivableContract.setStaffName(sysUser.getUsername());
//
//        String customerId = receivableContract.getCustomerId();
//        CustomerUserInfo customerUserInfo = iCustomerUserInfoService.getById(customerId);
//        receivableContract.setCustomerName(customerUserInfo.getCustomerName());
//
//        boolean b = iReceivableContractService.saveOrUpdate(receivableContract);
//        if (b){
//            //更新成功
//            return new Result();
//        }else {
//            return new Result(OPERATE_ERROR);
//        }
//    }

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
     * 根据合同ID查询合同详细信息
     *
     * @param contractId
     * @return
     */
    @GetMapping("/contractFindById/{contractId}")
    @ApiOperation(value = "查询详细合同信息", notes = "contractId存在")
    @ApiImplicitParam(name = "contractId", value = "合同编号", required = true, dataType = "String")
    public Result<ReceivableContractDTO> findById(@PathVariable String contractId) {
        if (StringUtils.isBlank(contractId)) {
            return new Result<>();
        } else {
            ReceivableContractDTO receivableContractDTO = new ReceivableContractDTO();
            receivableContractDTO.setContractId(contractId);
            return new Result<>(iReceivableContractService.findByContractId(receivableContractDTO));
        }
    }

    /**
     * 根据合同客户编号查询合同信息
     * @param customerId
     * @return
     */
    @GetMapping("/byCustomerId/{customerId}")
    @ApiOperation(value = "查询合同信息", notes = "customerId存在")
    @ApiImplicitParam(name = "customerId", value = "客户编号", required = true, dataType = "String")
    public Result findByCustomerId(@PathVariable String customerId) {
        if (StringUtils.isBlank(customerId)) {
            return new Result<>();
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("customer_id", customerId);
            return new Result<>(iReceivableContractService.listByMap(map));
        }
    }

//    /**
//     * 根据合同ID获取合同详情
//     * @param contractId
//     * @return
//     */
//    @GetMapping("/{contractId}")
//    public Result get(@PathVariable String contractId) {
//        if (StringUtils.isBlank(contractId)){
//            return new Result(CommonEnums.PARAM_ERROR,"参数错误");
//        }
//        ReceivableContract contract_info = iReceivableContractService.getById(contractId);
//        return new Result(contract_info);

//    }

}
