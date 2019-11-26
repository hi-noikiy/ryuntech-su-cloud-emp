package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.CustomerMonitor;
import com.ryuntech.saas.api.model.CustomerUserInfo;

/**
 * <p>
 * 客户风险监控表 服务类
 * </p>
 *
 * @author antu
 * @since 2019-11-20
 */
public interface ICustomerMonitorService extends IBaseService<CustomerMonitor> {

    /**
     * 分页查询
     * @param customerMonitor
     * @param queryPage
     * @return
     */
    Result<IPage<CustomerMonitor>> pageList(CustomerMonitor customerMonitor, QueryPage queryPage);
}
