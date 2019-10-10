package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.mapper.ReceivableContractMapper;
import com.ryuntech.saas.api.model.PaymentResult;
import com.ryuntech.saas.api.model.ReceivableContract;
import com.ryuntech.saas.api.service.IReceivableContractService;
import org.springframework.stereotype.Service;

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

    @Override
    public Result<IPage<ReceivableContract>> pageList(ReceivableContract receivableContract, QueryPage queryPage) {
        Page<ReceivableContract> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        if (receivableContract.getCollectionId()!=null) {
            queryWrapper.eq("collection_id", receivableContract.getCollectionId());
        }
        return super.pageList(queryWrapper,page);
    }

    @Override
    public Result<IPage<ReceivableContract>> selectPageList(ReceivableContract receivableContract, QueryPage queryPage) {
        Page<ReceivableContract> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        return new Result(m.selectPageList(page,receivableContract));
    }
}
