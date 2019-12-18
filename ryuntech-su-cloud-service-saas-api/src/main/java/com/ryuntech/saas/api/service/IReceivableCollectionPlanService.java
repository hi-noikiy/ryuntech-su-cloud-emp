package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.form.ReceivableCollectionPlanForm;
import com.ryuntech.saas.api.model.Attachment;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import com.ryuntech.saas.api.model.ReceivableContract;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author antu
 * @since 2019-09-29
 */
public interface IReceivableCollectionPlanService extends IBaseService<ReceivableCollectionPlan> {

    /**
     * 分页查询
     * @param receivableCollectionPlan
     * @param queryPage
     * @return
     */
    Result<IPage<ReceivableCollectionPlan>> pageList(ReceivableCollectionPlan receivableCollectionPlan, QueryPage queryPage);

    /**
     * 分页查询
     * @param receivableCollectionPlan
     * @param queryPage
     * @return
     */
    Result<IPage<ReceivableCollectionPlan>> selectPageList(ReceivableCollectionPlan receivableCollectionPlan, QueryPage queryPage);


    /**
     * 批量插入
     * @param receivableCollectionPlans
     * @return
     */
    Boolean insertBatch(List<ReceivableCollectionPlan> receivableCollectionPlans);


    /**
     * 查询回款计划
     * @param receivableCollectionPlanForm
     * @return
     */
    List<ReceivableCollectionPlan> selectByPlan(ReceivableCollectionPlanForm receivableCollectionPlanForm);

    /**
     * 定时查询逾期计划
     * @param receivableCollectionPlanForm
     * @return
     */
    List<ReceivableCollectionPlan> selectOverdueRemind(ReceivableCollectionPlanForm receivableCollectionPlanForm);
}
