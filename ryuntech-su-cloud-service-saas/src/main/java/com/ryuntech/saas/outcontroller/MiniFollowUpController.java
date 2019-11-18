package com.ryuntech.saas.outcontroller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.model.BaseDto;
import com.ryuntech.common.model.BaseForm;
import com.ryuntech.common.utils.CopyUtil;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.ContractRecordDTO;
import com.ryuntech.saas.api.dto.ReceivableContractDTO;
import com.ryuntech.saas.api.form.*;
import com.ryuntech.saas.api.helper.constant.PlanConstant;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.IFollowupRecordCommentService;
import com.ryuntech.saas.api.service.IFollowupRecordService;
import com.ryuntech.saas.api.service.IReceivableCollectionPlanService;
import com.ryuntech.saas.api.service.IReceivableContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;

/**
 * @author EDZ
 */
@Slf4j
@RestController
@RequestMapping("/minifollowup")
@Api(value = "MiniFollowUpController", tags = {"应收跟进记录"})
public class MiniFollowUpController extends ModuleBaseController{


    @Autowired
    private IFollowupRecordService followupRecordService;

    @Autowired
    private IReceivableCollectionPlanService iReceivableCollectionPlanService;

    @Autowired
    private IReceivableContractService iReceivableContractService;


    @Autowired
    private IFollowupRecordCommentService iFollowupRecordCommentService;

    @PostMapping("/outlist")
    @ApiOperation(value = "分页、条件查询用户列表信息")
    @ApiImplicitParam(name = "followupRecord", value = "查询条件", required = true, dataType = "FollowupRecord", paramType = "body")
    public Result<IPage<FollowupRecord>> list(@RequestBody FollowupRecord followupRecord,  QueryPage queryPage) {
        log.info("followupRecord.getContractId()"+followupRecord.getFollowupId());
        if (StringUtils.isBlank(followupRecord.getContractId())) {
            return new Result<>(OPERATE_ERROR,"合同编号不能为空");
        } else {
            return  followupRecordService.pageList(followupRecord, queryPage);
        }
    }

    /**
     * 添加合同跟进信息
     *
     * @param followupRecord
     * @return
     */
    @PostMapping("/outadd")
    @ApiOperation(value = "添加跟进信息")
    @ApiImplicitParam(name = "followupRecord", value = "合同跟进信息", required = true, dataType = "FollowupRecord", paramType = "body")
    public Result add(@RequestBody FollowupRecord followupRecord) {
        if (StringUtils.isNotBlank(followupRecord.getContractId())){
//            生成主键
            long followupId = generateId();
            followupRecord.setFollowupId(String.valueOf(followupId));
            followupRecord.setFollowupTime(new Date());
            followupRecord.setUpdatedAt(new Date());
            followupRecord.setCreatedAt(new Date());
            boolean save = followupRecordService.saveOrUpdate(followupRecord);
            if (save){
                return new Result();
            }else {
                return new Result(OPERATE_ERROR);
            }
        }else {
            return new Result(OPERATE_ERROR);
        }
    }
    /**
     * 添加合同跟进信息
     *
     * @param contractRecordForm
     * @return
     */
    @PostMapping("/outcontractrlist")
    @ApiOperation(value = "合同跟进列表")
    @ApiImplicitParam(name = "contractRecordForm", value = "合同跟进信息", required = true, dataType = "ContractRecordForm", paramType = "body")
    public Result<Map<String,Object>> contractRecordList(@RequestBody ContractRecordForm contractRecordForm) {

//        总待跟进
        List<ContractRecordDTO> followupRecords = followupRecordService.contractRecordList(contractRecordForm);
//       今天待跟进，获取提醒类型 type=1为今日到期
        ReceivableCollectionPlanForm receivableCollectionPlanForm = new ReceivableCollectionPlanForm();
        receivableCollectionPlanForm.setPlanTime(new Date());
        receivableCollectionPlanForm.setStatus(PlanConstant.OVERDUED);
        List<ReceivableCollectionPlan> receivableCollectionPlans = iReceivableCollectionPlanService.selectByPlan(receivableCollectionPlanForm);

        List<ReceivableContract> receivableContracts = new ArrayList<>();

        for (ReceivableCollectionPlan receivableCollectionPlan :receivableCollectionPlans){
            String contractId = receivableCollectionPlan.getContractId();
            ReceivableContract byContract = iReceivableContractService.findByContract(new ReceivableContractForm().setContractId(contractId));
//            去除重复的合同
            boolean contains = receivableContracts.contains(byContract);
            if (!contains&&byContract!=null){
                receivableContracts.add(byContract);
            }
        }
        Map<String,Object> resultMap = new HashMap<>();
//        今日待跟进
        resultMap.put("followupRecords",followupRecords);
//        合同列表
        resultMap.put("receivableContracts",receivableContracts);


        return new Result<>(resultMap);
    }

//    addfrecordcomments添加评论信息

    /**
     * 添加合同跟进评论信息
     *
     * @param fRecordCommentForm
     * @return
     */
    @PostMapping("/outaddfrecordcs")
    @ApiOperation(value = "合同跟进列表")
    @ApiImplicitParam(name = "contractRecordForm", value = "合同跟进信息", required = true, dataType = "ContractRecordForm", paramType = "body")
    public Result<Map<String,Object>> addfrecordcomments(@RequestBody FRecordCommentForm fRecordCommentForm) {
        BaseForm baseForm = new BaseForm();
        baseForm.setAClass(FRecordCommentForm.class);
        baseForm.setT(fRecordCommentForm);

        BaseDto baseDto = new BaseDto();
        baseDto.setAClass(FollowupRecordComment.class);
        FollowupRecordComment followupRecordComment = new FollowupRecordComment();
        baseDto.setT(followupRecordComment);

        CopyUtil.copyObject2(baseForm,baseDto);


//        生成主键
        followupRecordComment.setCommentId(String.valueOf(generateId()));
        followupRecordComment.setCreatedAt(new Date());
        followupRecordComment.setUpdatedAt(new Date());
        followupRecordComment.setCommentTime(new Date());

        Boolean aBoolean = iFollowupRecordCommentService.addFollowupRecordComment(followupRecordComment);
        if (aBoolean){
            return new Result<>();
        }else {
            return new Result<>(OPERATE_ERROR);
        }
    }


}
























