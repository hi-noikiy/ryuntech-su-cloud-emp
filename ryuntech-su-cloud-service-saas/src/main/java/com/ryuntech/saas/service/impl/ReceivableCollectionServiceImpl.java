package com.ryuntech.saas.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.form.ReceivableCollectionConditionForm;
import com.ryuntech.saas.api.mapper.ReceivableCollectionMapper;
import com.ryuntech.saas.api.mapper.ReceivableCollectionPlanMapper;
import com.ryuntech.saas.api.mapper.ReceivableContractMapper;
import com.ryuntech.saas.api.model.ReceivableCollection;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import com.ryuntech.saas.api.model.ReceivableContract;
import com.ryuntech.saas.api.service.IReceivableCollectionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    ReceivableCollectionPlanMapper receivableCollectionPlanMapper;

    @Autowired
    ReceivableContractMapper receivableContractMapper;



    @Override
    public Result<IPage<ReceivableCollection>> pageList(ReceivableCollection receivableCollection, QueryPage queryPage) {
        Page<ReceivableCollection> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        if (receivableCollection.getCollectionId()!=null) {
            queryWrapper.eq("collection_id", receivableCollection.getCollectionId());
        }
        return super.pageList(queryWrapper,page);
    }

    @Override
    public Result<IPage<ReceivableCollection>> selectPageList(ReceivableCollectionConditionForm receivableCollectionConditionForm, QueryPage queryPage) {
        Page<ReceivableCollection> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        return new Result(m.selectPageList(page,receivableCollectionConditionForm));
    }

    @Override
    public ReceivableCollection selectByReceivableCollection(ReceivableCollection receivableCollection) {
        return null;
    }

    @Override
    public Boolean addReceivableCollection(
            ReceivableCollectionPlan receivableCollectionPlan,
            ReceivableCollection receivableCollection,
            ReceivableContract receivableContract) {
        if (null!=receivableCollection){
            baseMapper.insert(receivableCollection);
        }
        if (null!=receivableCollectionPlan && StringUtils.isNotBlank(receivableCollectionPlan.getPlanId())){
//            更新计划
            receivableCollectionPlanMapper.updateById(receivableCollectionPlan);
        }
//        更改合同表回款金额
        if (null!=receivableContract && StringUtils.isNotBlank(receivableContract.getContractId())){
            receivableContractMapper.updateById(receivableContract);
        }
        return true;
    }

}
