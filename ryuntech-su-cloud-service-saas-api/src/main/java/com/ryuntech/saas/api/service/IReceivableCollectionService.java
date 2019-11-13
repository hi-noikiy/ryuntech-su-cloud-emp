package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.form.ReceivableCollectionConditionForm;
import com.ryuntech.saas.api.model.ReceivableCollection;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
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
     * @param receivableCollectionConditionForm
     * @param queryPage
     * @return
     */
    Result<IPage<ReceivableCollection>> selectPageList(ReceivableCollectionConditionForm receivableCollectionConditionForm, QueryPage queryPage);

    /**
     * 查询回款数据
     * @param receivableCollection
     * @return
     */
    ReceivableCollection selectByReceivableCollection(ReceivableCollection receivableCollection);

    /**
     * 增加回款
     * @param receivableCollectionPlan
     * @param receivableCollection
     * @param receivableContract
     * @return
     */
    Boolean addReceivableCollection(
            ReceivableCollectionPlan receivableCollectionPlan,
            ReceivableCollection receivableCollection,
            ReceivableContract receivableContract
    );
}
