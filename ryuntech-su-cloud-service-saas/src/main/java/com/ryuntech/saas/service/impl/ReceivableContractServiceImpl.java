package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.ReceivableCollectionPlanDTO;
import com.ryuntech.saas.api.dto.ReceivableContractDTO;
import com.ryuntech.saas.api.mapper.AttachmentMapper;
import com.ryuntech.saas.api.mapper.ReceivableCollectionPlanMapper;
import com.ryuntech.saas.api.mapper.ReceivableContractMapper;
import com.ryuntech.saas.api.mapper.SysUserMapper;
import com.ryuntech.saas.api.model.Attachment;
import com.ryuntech.saas.api.model.PaymentResult;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import com.ryuntech.saas.api.model.ReceivableContract;
import com.ryuntech.saas.api.service.IReceivableContractService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        attachmentMapper.insertBatch(attachments);
//        插入计划数据
        receivableCollectionPlanMapper.insertBatch(receivableCollectionPlans);
        return true;
    }
}
