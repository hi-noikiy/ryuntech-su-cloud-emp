package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.ContractRecordDTO;
import com.ryuntech.saas.api.form.ContractRecordForm;
import com.ryuntech.saas.api.mapper.FollowupRecordCommentMapper;
import com.ryuntech.saas.api.mapper.FollowupRecordMapper;
import com.ryuntech.saas.api.model.FinanceUserInfo;
import com.ryuntech.saas.api.model.FollowupRecord;
import com.ryuntech.saas.api.model.FollowupRecordComment;
import com.ryuntech.saas.api.service.IFollowupRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
