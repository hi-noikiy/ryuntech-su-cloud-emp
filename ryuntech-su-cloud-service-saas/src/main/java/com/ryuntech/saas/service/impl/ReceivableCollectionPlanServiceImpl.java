package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.form.ReceivableCollectionPlanForm;
import com.ryuntech.saas.api.mapper.ReceivableCollectionPlanMapper;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import com.ryuntech.saas.api.model.ReceivableContract;
import com.ryuntech.saas.api.service.IReceivableCollectionPlanService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-09-29
 */
@Service
public class ReceivableCollectionPlanServiceImpl extends BaseServiceImpl<ReceivableCollectionPlanMapper, ReceivableCollectionPlan> implements IReceivableCollectionPlanService {

    @Override
    public Result<IPage<ReceivableCollectionPlan>> pageList(ReceivableCollectionPlan receivableCollectionPlan, QueryPage queryPage) {
        Page<ReceivableCollectionPlan> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        if (receivableCollectionPlan.getPlanId()!=null) {
            queryWrapper.eq("plan_id", receivableCollectionPlan.getPlanId());
        }
        return super.pageList(queryWrapper,page);
    }

    @Override
    public Result<IPage<ReceivableCollectionPlan>> selectPageList(ReceivableCollectionPlan receivableCollectionPlan, QueryPage queryPage) {
        Page<ReceivableCollectionPlan> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        return new Result(m.selectPageList(page,receivableCollectionPlan));
    }

    @Override
    public Boolean insertBatch(List<ReceivableCollectionPlan> receivableCollectionPlans) {
        if (baseMapper.insertBatch(receivableCollectionPlans)>0){
            return true;
        }
        return false;
    }

    @Override
    public List<ReceivableCollectionPlan> selectByPlan(ReceivableCollectionPlanForm receivableCollectionPlanForm) {

        List<ReceivableCollectionPlan> receivableCollectionPlans = baseMapper.selectByPlan(receivableCollectionPlanForm);
        return receivableCollectionPlans;
    }

    @Override
    public List<ReceivableCollectionPlan> selectOverdueRemind(ReceivableCollectionPlanForm receivableCollectionPlanForm) {

        return baseMapper.selectOverdueRemind(receivableCollectionPlanForm);
    }
}
