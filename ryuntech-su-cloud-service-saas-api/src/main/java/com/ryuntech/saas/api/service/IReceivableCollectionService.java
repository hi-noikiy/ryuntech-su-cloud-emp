package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.ReceivableCollection;
import com.ryuntech.saas.api.model.ReceivableContract;

/**
 * <p>
 * 回款表 服务类
 * </p>
 *
 * @author antu
 * @since 2019-09-27
 */
public interface IReceivableCollectionService extends IBaseService<ReceivableCollection> {

    /**
     * 分页查询
     * @param receivableCollection
     * @param queryPage
     * @return
     */
    Result<IPage<ReceivableCollection>> pageList(ReceivableCollection receivableCollection, QueryPage queryPage);

    /**
     * 分页查询
     * @param receivableCollection
     * @param queryPage
     * @return
     */
    Result<IPage<ReceivableCollection>> selectPageList(ReceivableCollection receivableCollection, QueryPage queryPage);
}
