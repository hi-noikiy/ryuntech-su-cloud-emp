package com.ryuntech.saas.service.impl;

import com.ryuntech.saas.api.form.ReceivableCollectionPlanForm;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import com.ryuntech.saas.api.service.IRContractScheduleService;
import com.ryuntech.saas.api.service.IReceivableCollectionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

/**
 * @author EDZ
 */
public class IRContractScheduleServiceImpl implements IRContractScheduleService {
    @Autowired
    IReceivableCollectionPlanService iReceivableCollectionPlanService;


    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void overdueRemind() {
//        获取当前时间
        Date date = new Date();
//        查询计划表
        List<ReceivableCollectionPlan> receivableCollectionPlans
                = iReceivableCollectionPlanService.selectOverdueRemind(new ReceivableCollectionPlanForm().setPlanTime(date));
    }
}
