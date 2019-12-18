package com.ryuntech.saas.api.service;

/**
 * 跟进合同逾期修改
 * @author EDZ
 */
public interface IRContractScheduleService {
    /**
     * 定时修改合同，计划逾期状态
     */
    void overdueRemind();
}
