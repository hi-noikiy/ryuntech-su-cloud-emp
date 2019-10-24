package com.ryuntech.saas.outcontroller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.ReceivableCollectionPlanDTO;
import com.ryuntech.saas.api.dto.ReceivableContractDTO;
import com.ryuntech.saas.api.helper.constant.AttachmentConstants;
import com.ryuntech.saas.api.helper.constant.ReceivableContractConstants;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.ICustomerUserInfoService;
import com.ryuntech.saas.api.service.IReceivableContractService;
import com.ryuntech.saas.api.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
     * 根据ID查询用户信息
     *
     * @param contractId
     * @return
     */
    @GetMapping("/outFindById")
    @ApiOperation(value = "查询详细融资客户信息", notes = "contractId存在")
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
//        合同编号
        String contractId = String.valueOf(generateId());
        receivableContract.setContractId(contractId);
//        合同名称
        receivableContract.setContractName(receivableContractDTO.getContractName());
        //        合同金额
        receivableContract.setBalanceAmount(receivableContractDTO.getContractAmount());
//        回款余额
        receivableContract.setCollectionAmount("0.00");
//       负责人编号
        String staffId = receivableContractDTO.getStaffId();
        if (StringUtils.isNotBlank(staffId)){
            SysUser sysUser = sysUserService.getById(staffId);
            receivableContract.setStaffName(sysUser.getUsername());
        }
        //       客户编号
        String customerId = receivableContractDTO.getCustomerId();
        if (StringUtils.isNotBlank(customerId)){
            CustomerUserInfo customerUserInfo = iCustomerUserInfoService.getById(customerId);
            receivableContract.setCustomerName(customerUserInfo.getCustomerName());
        }
//        合同状态
        receivableContract.setStatus(ReceivableContractConstants.NOTSTARTED);

        List<ReceivableCollectionPlan> receivableCollectionPlans = new ArrayList<>();
        List<ReceivableCollectionPlanDTO> receivableCollectionPlanDTOs = receivableContractDTO.getReceivableCollectionPlanDTOs();
        if (receivableCollectionPlanDTOs!=null&&receivableCollectionPlanDTOs.size()!=0){
            for (ReceivableCollectionPlanDTO receivableCollectionPlanDTO :receivableCollectionPlanDTOs){
                ReceivableCollectionPlan receivableCollectionPlan = new ReceivableCollectionPlan();
                receivableCollectionPlan.setContractId(contractId);
//                计划编号
                String planId = String.valueOf(generateId());
                receivableCollectionPlan.setPlanId(planId);
                receivableCollectionPlan.setPlanAmount(receivableCollectionPlanDTO.getPlanAmount());
                receivableCollectionPlan.setRemakes(receivableCollectionPlanDTO.getRemakes());
                receivableCollectionPlan.setPlanTime(DateUtil.parseDate(receivableCollectionPlanDTO.getPlanTime()));
                receivableCollectionPlans.add(receivableCollectionPlan);
            }
        }

        List<AttachmentFile> attachmentFiles = receivableContractDTO.getFiles();
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
}
