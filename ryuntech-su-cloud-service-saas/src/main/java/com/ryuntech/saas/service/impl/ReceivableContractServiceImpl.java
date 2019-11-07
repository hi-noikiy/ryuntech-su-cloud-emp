package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.DateUtil;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.ReceivableCollectionPlanDTO;
import com.ryuntech.saas.api.dto.ReceivableContractDTO;
import com.ryuntech.saas.api.mapper.AttachmentMapper;
import com.ryuntech.saas.api.mapper.FollowupRecordMapper;
import com.ryuntech.saas.api.mapper.ReceivableCollectionPlanMapper;
import com.ryuntech.saas.api.mapper.ReceivableContractMapper;
import com.ryuntech.saas.api.model.Attachment;
import com.ryuntech.saas.api.model.FollowupRecord;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import com.ryuntech.saas.api.model.ReceivableContract;
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




    @Override
    public Result<IPage<ReceivableContract>> pageList(ReceivableContract receivableContract, QueryPage queryPage) {
        Page<ReceivableContract> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        if (StringUtils.isNotBlank(receivableContract.getContractId())) {
            queryWrapper.eq("contract_id", receivableContract.getContractId());
        }
        return super.pageList(queryWrapper,page);
    }

    @Override
    public Result<IPage<ReceivableContract>> selectPageList(ReceivableContract receivableContract, QueryPage queryPage) {
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
//        attachmentMapper.insertBatch(attachments);
//        插入计划数据
        receivableCollectionPlanMapper.insertBatch(receivableCollectionPlans);
        return true;
    }

    @Override
    public ReceivableContractDTO findByContractId(ReceivableContractDTO receivableContractDTO) {

        //        计划
        List<ReceivableCollectionPlanDTO> receivableCollectionPlanDTOs = new ArrayList<>();
        //        合同对象
        ReceivableContract receivableContract = null;
        if (StringUtils.isNotBlank(receivableContractDTO.getContractId())){
            queryWrapper.eq("contract_id", receivableContractDTO.getContractId());
            QueryWrapper<ReceivableCollectionPlan> queryWrapper2 =new QueryWrapper<>();
            List<ReceivableCollectionPlan> receivableCollectionPlans= receivableCollectionPlanMapper.selectList(queryWrapper2);
            if (receivableCollectionPlans!=null&&receivableCollectionPlans.size()!=0){
                for (ReceivableCollectionPlan receivableCollectionPlan:receivableCollectionPlans){
                    ReceivableCollectionPlanDTO receivableCollectionPlanDTO =new ReceivableCollectionPlanDTO();
                    receivableCollectionPlanDTO.setPlanAmount(receivableCollectionPlan.getPlanAmount());
                    if (receivableCollectionPlan.getPlanTime()!=null){
                        receivableCollectionPlanDTO.setPlanTime(DateUtil.formatDateTime(receivableCollectionPlan.getPlanTime()));
                    }
                    receivableCollectionPlanDTO.setRemakes(receivableCollectionPlan.getRemakes());
                    receivableCollectionPlanDTO.setStatus(receivableCollectionPlan.getStatus());
                    receivableCollectionPlanDTOs.add(receivableCollectionPlanDTO);
                }

            }
            receivableContract = baseMapper.selectOne(queryWrapper);
            if (receivableContract!=null){
                receivableContractDTO.setAttachmentCode(receivableContract.getAttachmentCode());
                receivableContractDTO.setBalanceAmount(receivableContract.getBalanceAmount());
                receivableContractDTO.setCollectionAmount(receivableContract.getCollectionAmount());
                receivableContractDTO.setContacts(receivableContract.getContacts());
                receivableContractDTO.setContactsPhone(receivableContract.getContactsPhone());
                receivableContractDTO.setContractId(receivableContract.getContractId());
                receivableContractDTO.setContractName(receivableContract.getContractName());
                receivableContractDTO.setContractTime(receivableContract.getContractTime());
                receivableContractDTO.setContractCode(receivableContract.getContractCode());
                receivableContractDTO.setCustomerId(receivableContract.getCustomerId());
                receivableContractDTO.setContractName(receivableContract.getCustomerName());
                receivableContractDTO.setDepartment(receivableContract.getDepartment());
                receivableContractDTO.setStaffId(receivableContract.getStaffId());
                receivableContractDTO.setStaffName(receivableContract.getStaffName());
                receivableContractDTO.setStatus(receivableContract.getStatus());
            }
        }

//        计划
        receivableContractDTO.setReceivableCollectionPlanDTOs(receivableCollectionPlanDTOs);
//        跟进记录
        List<FollowupRecord> followupRecord2 = followupRecordMapper.selectList(new QueryWrapper<FollowupRecord>().eq("contract_id", receivableContractDTO.getContractId()));
        if (followupRecord2!=null&&followupRecord2.size()!=0){
            FollowupRecord followupRecord = followupRecord2.get(0);
            receivableContractDTO.setFollowupRecord(followupRecord);
        }
        return receivableContractDTO;
    }

    @Override
    public ReceivableContractDTO findDetailByContractId(ReceivableContractDTO receivableContractDTO) {
        return null;
    }
}
