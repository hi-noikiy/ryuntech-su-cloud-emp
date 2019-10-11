package com.ryuntech.saas.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.mapper.ReceivableCollectionMapper;
import com.ryuntech.saas.api.model.ReceivableCollection;
import com.ryuntech.saas.api.service.IReceivableCollectionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 回款表 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-09-27
 */
@Service
public class ReceivableCollectionServiceImpl extends BaseServiceImpl<ReceivableCollectionMapper, ReceivableCollection> implements IReceivableCollectionService {

    @Override
    public Result<IPage<ReceivableCollection>> pageList(ReceivableCollection receivableCollection, QueryPage queryPage) {
        Page<ReceivableCollection> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        if (receivableCollection.getCollectionId()!=null) {
            queryWrapper.eq("collection_id", receivableCollection.getCollectionId());
        }
        return super.pageList(queryWrapper,page);
    }

    @Override
    public Result<IPage<ReceivableCollection>> selectPageList(ReceivableCollection receivableCollection, QueryPage queryPage) {
        Page<ReceivableCollection> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        return new Result(m.selectPageList(page,receivableCollection));
    }

}
