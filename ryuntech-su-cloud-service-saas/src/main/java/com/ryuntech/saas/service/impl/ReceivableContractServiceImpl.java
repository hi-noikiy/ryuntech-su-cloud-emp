package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.model.BaseDto;
import com.ryuntech.common.model.BaseForm;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.CopyUtil;
import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.FollowupRecordDTO;
import com.ryuntech.saas.api.dto.ReceivableCollectionPlanDTO;
import com.ryuntech.saas.api.dto.ReceivableContractDTO;
import com.ryuntech.saas.api.form.ReceivableContractForm;
import com.ryuntech.saas.api.mapper.*;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.IReceivableContractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 应收合同表 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-09-27
 */
@Service
public class ReceivableContractServiceImpl extends BaseServiceImpl<ReceivableContractMapper, ReceivableContract> implements IReceivableContractService {


    @Autowired
    ReceivableCollectionPlanMapper receivableCollectionPlanMapper;

    @Autowired
    AttachmentMapper attachmentMapper;

    @Autowired
    FollowupRecordMapper followupRecordMapper;

    @Autowired
    ReceivableCollectionMapper receivableCollectionMapper;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    FollowupRecordCommentMapper followupRecordCommentMapper;


    @Override
    public Result<IPage<ReceivableContract>> pageList(ReceivableContract receivableContract, QueryPage queryPage) {
        Page<ReceivableContract> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        QueryWrapper<ReceivableContract> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(receivableContract.getContractId())) {
            queryWrapper.eq("contract_id", receivableContract.getContractId());
        }
        if (StringUtils.isNotBlank(receivableContract.getStatus())) {
            queryWrapper.eq("status", receivableContract.getStatus());
        }
        return super.pageList(queryWrapper,page);
    }

    @Override
    public Result<IPage<ReceivableContractDTO>> selectPageList(ReceivableContract receivableContract, QueryPage queryPage) {
        Page<ReceivableContract> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        return new Result(m.selectPageList(page,receivableContract));
    }

    @Override
    public List<ReceivableContract> receivableContractList(ReceivableContract receivableContract) {
        if (StringUtils.isNotBlank(receivableContract.getContractId())){
            queryWrapper.eq("contract_id", receivableContract.getContractId());
        }

        if (StringUtils.isNotBlank(receivableContract.getStaffId())){
            queryWrapper.eq("staff_id", receivableContract.getStaffId());
        }

        return m.selectList(queryWrapper);
    }

    @Override
    public Boolean addReceivableContract(List<Attachment> attachments,ReceivableContract receivableContract, List<ReceivableCollectionPlan> receivableCollectionPlans) {
        baseMapper.insert(receivableContract);
//        插入图片数据
        if (attachments!=null&&attachments.size()!=0){
            attachmentMapper.insertBatch(attachments);
        }
//        插入计划数据
        if (receivableCollectionPlans!=null&&receivableCollectionPlans.size()!=0){
            receivableCollectionPlanMapper.insertBatch(receivableCollectionPlans);
        }
        return true;
    }

    @Override
    public Boolean editReceivableContract(List<Attachment> attachments, ReceivableContract receivableContract, List<ReceivableCollectionPlan> receivableCollectionPlans) {

        if (null!=receivableContract){
            baseMapper.updateById(receivableContract);
        }
//        修改计划数据
        if (receivableCollectionPlans!=null&&receivableCollectionPlans.size()!=0){
            receivableCollectionPlanMapper.delete(
                    new QueryWrapper<ReceivableCollectionPlan>().
                            eq("contract_id",receivableContract.getContractId()));
            receivableCollectionPlanMapper.insertBatch(receivableCollectionPlans);
        }
        //        插入图片数据
        if (attachments!=null&&attachments.size()!=0){
            //删除图片数据
            attachmentMapper.delete(
                    new QueryWrapper<Attachment>().
                            eq("attachment_code",receivableContract.getAttachmentCode()));
            attachmentMapper.insertBatch(attachments);
        }
        return true;
    }

    @Override
    public ReceivableContractDTO findByContractId(ReceivableContractDTO receivableContractDTO) {

        //        计划
        List<ReceivableCollectionPlanDTO> receivableCollectionPlanDTOs = new ArrayList<>();

        if (StringUtils.isNotBlank(receivableContractDTO.getContractId())){
            QueryWrapper<ReceivableCollectionPlan> queryWrapper2 =new QueryWrapper<>();
            queryWrapper2.eq("contract_id", receivableContractDTO.getContractId());
            List<ReceivableCollectionPlan> receivableCollectionPlans= receivableCollectionPlanMapper.selectList(queryWrapper2);
            if (receivableCollectionPlans!=null&&receivableCollectionPlans.size()!=0){
                for (ReceivableCollectionPlan receivableCollectionPlan:receivableCollectionPlans){
                    ReceivableCollectionPlanDTO receivableCollectionPlanDTO =new ReceivableCollectionPlanDTO();
                    receivableCollectionPlanDTO.setPlanAmount(receivableCollectionPlan.getPlanAmount());
                    receivableCollectionPlanDTO.setBackAmount(receivableCollectionPlan.getBackedAmount());
                    receivableCollectionPlanDTO.setSurplusAmount(receivableCollectionPlan.getSurplusAmount());
                    receivableCollectionPlanDTO.setContractId(receivableContractDTO.getContractId());
                    receivableCollectionPlanDTO.setPlanId(receivableCollectionPlan.getPlanId());
                    if (receivableCollectionPlan.getPlanTime()!=null){
                        receivableCollectionPlanDTO.setPlanTime(DateUtil.formatDate(receivableCollectionPlan.getPlanTime()));
                    }
                    receivableCollectionPlanDTO.setRemakes(receivableCollectionPlan.getRemakes());
                    receivableCollectionPlanDTO.setStatus(receivableCollectionPlan.getStatus());
//                    查询已回款金额
                    receivableCollectionPlanDTOs.add(receivableCollectionPlanDTO);
                }

            }
            //        合同对象
            ReceivableContract receivableContract = baseMapper.selectOne(new QueryWrapper<ReceivableContract>().eq("contract_id", receivableContractDTO.getContractId()));
            if (receivableContract!=null){
                String attachmentCode = receivableContract.getAttachmentCode();
                receivableContractDTO.setAttachmentCode(attachmentCode);
                receivableContractDTO.setBalanceAmount(receivableContract.getBalanceAmount());
                receivableContractDTO.setCollectionAmount(receivableContract.getCollectionAmount());
                receivableContractDTO.setContacts(receivableContract.getContacts());
                receivableContractDTO.setContactsPhone(receivableContract.getContactsPhone());
                receivableContractDTO.setContractId(receivableContract.getContractId());
                receivableContractDTO.setContractName(receivableContract.getContractName());
                receivableContractDTO.setContractTime(receivableContract.getContractTime());
                receivableContractDTO.setContractCode(receivableContract.getContractCode());
                receivableContractDTO.setCustomerId(receivableContract.getCustomerId());

                receivableContractDTO.setCustomerName(receivableContract.getCustomerName());



                receivableContractDTO.setDepartment(receivableContract.getDepartment());
                receivableContractDTO.setStaffId(receivableContract.getStaffId());
                receivableContractDTO.setStaffName(receivableContract.getStaffName());
//                查询公司
                Employee employee = employeeMapper.selectOne(new QueryWrapper<Employee>().eq("employee_id", receivableContract.getStaffId()));
                if (employee != null) {
                    receivableContractDTO.setCompanyName(employee.getCompanyName());
                }
                receivableContractDTO.setStatus(receivableContract.getStatus());
//                合同总额
                receivableContractDTO.setContractAmount(receivableContract.getContractAmount());
//                合同状态
                receivableContractDTO.setStatus(receivableContract.getStatus());
                /**
                 * 附件长度
                 */
                List<Attachment> attachmentCodes = attachmentMapper.selectList(new QueryWrapper<Attachment>().eq("attachment_code", attachmentCode));
                if (!attachmentCodes.isEmpty()){
                    receivableContractDTO.setAttachmentCodeSize(attachmentCodes.size());
                }else {
                    receivableContractDTO.setAttachmentCodeSize(0);
                }
            }
        }

//        计划
        receivableContractDTO.setReceivableCollectionPlanDTOs(receivableCollectionPlanDTOs);
//        跟进记录
        List<FollowupRecord> followupRecord2 =
                followupRecordMapper
                        .selectList(new QueryWrapper<FollowupRecord>()
                                .eq("contract_id", receivableContractDTO.getContractId()).orderByDesc("followup_time"));
        if (followupRecord2!=null&&followupRecord2.size()!=0){
            // 将followupRecord2中的跟进记录复制到followupRecordDTOList中用于输出到页面
            List<FollowupRecordDTO> followupRecordDTOList = new ArrayList<>();
            BaseForm baseForm = new BaseForm();
            baseForm.setAClass(FollowupRecord.class);
            BaseDto baseDto = new BaseDto();
            baseDto.setAClass(FollowupRecordDTO.class);
            for(FollowupRecord followupRecord : followupRecord2) {
                baseForm.setT(followupRecord);
                FollowupRecordDTO followupRecordDTO = new FollowupRecordDTO();
                baseDto.setT(followupRecordDTO);
                CopyUtil.copyObject2(baseForm,baseDto);
                QueryWrapper<FollowupRecordComment> queryWrapper =new QueryWrapper<>();
                queryWrapper.eq("followup_id", followupRecordDTO.getFollowupId());
                List<FollowupRecordComment> followupRecordCommentList = followupRecordCommentMapper.selectList(queryWrapper);
                if(followupRecordCommentList != null && followupRecordCommentList.size() != 0) {
                    followupRecordDTO.setFollowupRecordComments(followupRecordCommentList);
                }
                followupRecordDTOList.add(followupRecordDTO);
            }

            FollowupRecordDTO followupRecordDTO = followupRecordDTOList.get(0);
            receivableContractDTO.setFollowUpRecord(followupRecordDTO);
            receivableContractDTO.setFollowUpRecords(followupRecordDTOList);
        }
        return receivableContractDTO;
    }

    @Override
    public ReceivableContract findByContract(ReceivableContractForm receivableContractForm) {
        queryWrapper= new QueryWrapper<>();
        if (StringUtils.isNotBlank(receivableContractForm.getContractCode())) {
            queryWrapper.eq("contract_code", receivableContractForm.getContractCode());
        }
        if (StringUtils.isNotBlank(receivableContractForm.getContractId())) {
            queryWrapper.eq("contract_id", receivableContractForm.getContractId());
        }
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public ReceivableContractDTO findDetailByContractId(ReceivableContractDTO receivableContractDTO) {
        return null;
    }

    @Override
    public String selectSumByRContract(ReceivableContractDTO receivableContractDTO) {
        return baseMapper.selectSumByRContract(receivableContractDTO);
    }
}
