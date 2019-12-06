package com.ryuntech.saas.api.service;




/**
 * @author EDZ
 */
public interface PushMessageScheduleService {

    /**
     * 计划到期提醒
     */
    void planExpirePush();

    /**
     * 风险监控日报提醒
     * @throws Exception
     */
    void riskMonitorPush() throws Exception;

    /**
     * 逾期应收提醒
     */
    void overdueRPush();
}
