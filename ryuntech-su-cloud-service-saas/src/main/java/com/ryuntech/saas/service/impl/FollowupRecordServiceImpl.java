package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.ContractRecordDTO;
import com.ryuntech.saas.api.dto.FollowupRecordDTO;
import com.ryuntech.saas.api.form.ContractRecordForm;
import com.ryuntech.saas.api.form.FollowupRecordForm;
import com.ryuntech.saas.api.helper.constant.AttachmentConstants;
import com.ryuntech.saas.api.mapper.AttachmentMapper;
import com.ryuntech.saas.api.mapper.FollowupRecordCommentMapper;
import com.ryuntech.saas.api.mapper.FollowupRecordMapper;
import com.ryuntech.saas.api.model.*;
import com.ryuntech.saas.api.service.IFollowupRecordService;
import com.ryuntech.saas.api.service.IReceivableContractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ryuntech.common.constant.generator.UniqueIdGenerator.generateId;

/**
 * <p>
 * 跟进表 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-10-24
 */
@Service
public class FollowupRecordServiceImpl extends BaseServiceImpl<FollowupRecordMapper, FollowupRecord> implements IFollowupRecordService {

    @Autowired
    private FollowupRecordCommentMapper followupRecordCommentMapper;

    @Autowired
    private FollowupRecordMapper followupRecordMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private IReceivableContractService iReceivableContractService;

    @Override
    public Result<IPage<FollowupRecord>> pageList(FollowupRecord followupRecord, QueryPage queryPage) {
        Page<FollowupRecord> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        if (followupRecord.getFollowupId()!=null) {
            queryWrapper.eq("followup_id", followupRecord.getFollowupId());
        }
        if (followupRecord.getContractId()!=null) {
            queryWrapper.eq("contract_id", followupRecord.getContractId());
        }
        return super.pageList(queryWrapper,page);
    }

    @Override
    public Result<IPage<FollowupRecordDTO>> selectPageList(FollowupRecordForm followupRecordForm, QueryPage queryPage) {
        Page<FollowupRecord> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        return new Result(m.selectPageList(page,followupRecordForm));
    }

    @Override
    public List<ContractRecordDTO> contractRecordList(ContractRecordForm contractRecordForm) {
        List<ContractRecordDTO> contractRecordDTOS = baseMapper.contractRecordList(contractRecordForm);
        for (ContractRecordDTO contractRecordDTO :contractRecordDTOS ){
            String followupId = contractRecordDTO.getFollowupId();
            List<FollowupRecordComment> followupRecords = followupRecordCommentMapper.selectList(
                    new QueryWrapper<FollowupRecordComment>().eq("followup_id", followupId));
            contractRecordDTO.setFollowupRecordComments(followupRecords);
        }
        return contractRecordDTOS;
    }

    @Override
    public Boolean addFollowupRecord(FollowupRecordForm followupRecordForm) {
        FollowupRecord followupRecord = new FollowupRecord();
        followupRecord.setContractId(followupRecordForm.getContractId());
        followupRecord.setFollowupId(followupRecordForm.getFollowupId());
        followupRecord.setContent(followupRecordForm.getContent());
        followupRecord.setContractName(followupRecordForm.getContractName());
        followupRecord.setEstimateAmount(followupRecordForm.getEstimateAmount());
        followupRecord.setEstimateTime(followupRecordForm.getEstimateTime());
        if (StringUtils.isBlank(followupRecordForm.getStaffId())){
            ReceivableContract byId = iReceivableContractService.getById(followupRecordForm.getContractId());
            followupRecord.setStaffId(byId.getStaffId());
            followupRecord.setStaffName(byId.getStaffName());
        }else {
            followupRecord.setStaffId(followupRecordForm.getStaffId());
            followupRecord.setStaffName(followupRecordForm.getStaffName());
        }
        followupRecord.setFollowupTime(new Date());
        followupRecord.setUpdatedAt(new Date());
        followupRecord.setCreatedAt(new Date());
        /**
         * 开始插入图片数据
         */
        String[] upLoadImg = followupRecordForm.getUpLoadImg();
        List<Attachment> attachmentList = new ArrayList<>();
        if (null!=upLoadImg&&upLoadImg.length!=0){
            String attachmentCode = String.valueOf(generateId());
            for (String url:upLoadImg){
                long attachmentId = generateId();
                Attachment attachment = new Attachment();
                attachment.setId(String.valueOf(attachmentId));
                attachment.setAttachmentType(AttachmentConstants.TYPE1);
                attachment.setStatus(AttachmentConstants.YES);
                attachment.setAttachmentUrl(url);
                attachment.setCreatedAt(new Date());
                attachment.setUpdatedAt(new Date());
                attachment.setAttachmentCode(attachmentCode);
                attachmentList.add(attachment);
            }
            attachmentMapper.insertBatch(attachmentList);
            followupRecord.setAttachmentCode(attachmentCode);
        }
        int insert = followupRecordMapper.insert(followupRecord);
        if (insert>=0){
            return true;
        }
        return false;
    }
}
