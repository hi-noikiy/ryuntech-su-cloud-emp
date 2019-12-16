package com.ryuntech.saas.outcontroller;


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
import com.ryuntech.saas.api.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    private IEmployeeService iEmployeeService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ICustomerUserInfoService iCustomerUserInfoService;


    private IReceivableCollectionPlanService  iReceivableCollectionPlanService;

    @PostMapping("/outlist")
    @ApiOperation(value = "分页、条件查询合同信息")
    @ApiImplicitParam(name = "receivableContract", value = "查询条件", required = true, dataType = "ReceivableContract", paramType = "body")
    public Result<IPage<ReceivableContractDTO>> list(@RequestBody ReceivableContract receivableContract, QueryPage queryPage) {
       /* log.info("receivableContract.getContractId()"+receivableContract.getContractId());
        log.info("receivableContract.getStatus()"+receivableContract.getStatus());
        if(StringUtils.isBlank(EmployeeId)){
            return new Result(OPERATE_ERROR,"用户未登陆");
        }*/
        if (StringUtils.isNotBlank(receivableContract.getStatus())){
            receivableContract.setStatusList(Arrays.asList(receivableContract.getStatus().split("_")));
        }
//        receivableContract.setStaffId(EmployeeId);
        log.info("receivableContract.getStatusList()"+receivableContract.getStatusList());
        return  iReceivableContractService.selectPageList(receivableContract, queryPage);
    }


    /**
     * 根据ID查询用户信息
     *
     * @param contractId
     * @return
     */
    @GetMapping("/outFindById")
    @ApiOperation(value = "查询详细合同信息", notes = "contractId存在")
    @ApiImplicitParam(name = "contractId", value = "合同编号", required = true, dataType = "String")
    public Result<ReceivableContractDTO> findById(String contractId) {
        if (StringUtils.isBlank(contractId)) {
            return new Result<>();
        } else {
            ReceivableContractDTO receivableContractDTO = new ReceivableContractDTO();
            receivableContractDTO.setContractId(contractId);
            return new Result<>(iReceivableContractService.findByContractId(receivableContractDTO));
        }
    }

    /**
     * 根据ID查询用户信息
     *
     * @param contractId
     * @return
     */
    @GetMapping("/outDetailFindById")
    @ApiOperation(value = "查询详细合同信息", notes = "contractId存在")
    @ApiImplicitParam(name = "contractId", value = "合同编号", required = true, dataType = "String")
    public Result<ReceivableContractDTO> detailFindById(String contractId) {
        if (StringUtils.isBlank(contractId)) {
            return new Result<>();
        } else {
            ReceivableContractDTO receivableContractDTO = new ReceivableContractDTO();
            receivableContractDTO.setContractId(contractId);
            return new Result<>(iReceivableContractService.findByContractId(receivableContractDTO));
        }
    }


    @PostMapping("/outedit")
    @ApiOperation(value = "修改合同信息")
    @ApiImplicitParam(name = "receivableContractFrom", value = "合同实体信息", required = true, dataType = "ReceivableContractFrom", paramType = "body")
    public Result edit(@RequestBody ReceivableContractForm receivableContractFrom) {
        if (StringUtils.isBlank(receivableContractFrom.getContractId())){
            return new Result(PARAM_ERROR,"合同编号不能为空");
        }
        ReceivableContract receivableContract = new ReceivableContract();
//        合同编号
        String contractId = receivableContractFrom.getContractId();
        receivableContract.setContractId(contractId);
//        合同名称
        receivableContract.setContractName(receivableContractFrom.getContractName());
        //        待还款金额
        receivableContract.setBalanceAmount(receivableContractFrom.getBalanceAmount());
//        合同总额
        receivableContract.setContractAmount(receivableContractFrom.getContractAmount());
//        合同时间
        receivableContract.setContractTime(receivableContractFrom.getContractTime());
//        联系人
        receivableContract.setContacts(receivableContractFrom.getContacts());
//        联系人电话
        receivableContract.setContactsPhone(receivableContractFrom.getContactsPhone());
//        合同编码
        receivableContract.setContractCode(receivableContractFrom.getContractCode());
//        回款余额
        receivableContract.setCollectionAmount(receivableContractFrom.getCollectionAmount());
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
                }
                receivableCollectionPlan.setPlanId(planIdDT);
//                状态
                receivableCollectionPlan.setStatus(PlanConstant.NOTSTARTED);
                receivableCollectionPlan.setPlanAmount(receivableCollectionPlanDTO.getPlanAmount());
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
            return new Result(receivableContract);
        }else {
            return new Result(OPERATE_ERROR);
        }



    }


    /**
     * 添加合同信息
     *
     * @param receivableContractFrom
     * @return
     */
    @PostMapping("/outadd")
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
        //        待还款金额
        if(StringUtils.isNotBlank(receivableContractFrom.getBalanceAmount())){
            receivableContract.setBalanceAmount(receivableContractFrom.getBalanceAmount());
        }else {
            receivableContract.setBalanceAmount(receivableContractFrom.getContractAmount());
        }

//        合同总额
        receivableContract.setContractAmount(receivableContractFrom.getContractAmount());
//        合同时间
        receivableContract.setContractTime(receivableContractFrom.getContractTime());
//        联系人
        receivableContract.setContacts(receivableContractFrom.getContacts());
//        合同时间
        receivableContract.setContractTime(receivableContractFrom.getContractTime());
//        生成合同编码
        String contractCode = String.valueOf(generateId());
        receivableContract.setContractCode(contractCode);
//        回款余额
        receivableContract.setCollectionAmount("0.00");
        //       负责人编号
        receivableContract.setStaffId(receivableContractFrom.getStaffId());
        //        负责人姓名
        receivableContract.setStaffName(receivableContractFrom.getStaffName());
//
//        if (StringUtils.isNotBlank(staffId)){
//            SysUser sysUser = sysUserService.getById(staffId);
//            receivableContract.setStaffName(sysUser.getUsername());
//        }

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
//                计划编号
                String planId = String.valueOf(generateId());
                receivableCollectionPlan.setPlanId(planId);
//                状态
                receivableCollectionPlan.setStatus(PlanConstant.NOTSTARTED);
                receivableCollectionPlan.setPlanAmount(receivableCollectionPlanDTO.getPlanAmount());
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
            return new Result(receivableContract);
        }else {
            return new Result(OPERATE_ERROR);
        }

    }
    /**
     * 生成合同编号CONTRACT_CODE
     *
     * @param receivableContractFrom
     * @return
     */
    @PostMapping("/outcreatecode")
    @ApiOperation(value = "生成合同编号")
    @ApiImplicitParam(name = "receivableContractFrom", value = "合同实体信息", required = true, dataType = "ReceivableContractFrom", paramType = "body")
    public Result createcontract(@RequestBody ReceivableContractForm receivableContractFrom) {
//        生成合同编码
        String contractCode = String.valueOf(generateId());
        if (StringUtils.isBlank(contractCode)){
            return new Result(OPERATE_ERROR,"生成合同编号失败");
        }
        //验证编码是否存在
        receivableContractFrom.setContractCode(contractCode);
        ReceivableContract byContract = iReceivableContractService.findByContract(receivableContractFrom);
        if (byContract==null){
            return new Result(contractCode);
        }
        return new Result(OPERATE_ERROR,"生成合同编号失败");
    }

    @PostMapping("/outemployeelist")
    @ApiOperation(value = "分页、条件查询员工信息")
    @ApiImplicitParam(name = "receivableContract", value = "查询条件", required = true, dataType = "ReceivableContract", paramType = "body")
    public Result<IPage<Employee>> outemployeelist(@RequestBody Employee employee, QueryPage queryPage) {
        log.info("receivableContract.getContractId()"+employee.getEmployeeId());
        return  iEmployeeService.selectPageList(employee, queryPage);
    }


}
