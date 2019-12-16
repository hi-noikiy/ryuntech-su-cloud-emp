package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.mapper.CustomerMonitorMapper;
import com.ryuntech.saas.api.model.CustomerMonitor;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import com.ryuntech.saas.api.service.ICustomerMonitorService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户风险监控表 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-11-20
 */
@Service
public class CustomerMonitorServiceImpl extends BaseServiceImpl<CustomerMonitorMapper, CustomerMonitor> implements ICustomerMonitorService {

    @Override
    public Result<IPage<CustomerMonitor>> pageList(CustomerMonitor customerMonitor, QueryPage queryPage) {
        Page<CustomerMonitor> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        queryWrapper = new QueryWrapper<>();
        if (customerMonitor.getCustomerId()!=null) {
            queryWrapper.eq("customer_id", customerMonitor.getCustomerId());
        }
        if (customerMonitor.getMonitorId()!=null) {
            queryWrapper.eq("monitor_id", customerMonitor.getMonitorId());
        }
        if (customerMonitor.getEmployeeId()!=null) {
            queryWrapper.eq("employee_id", customerMonitor.getEmployeeId());
        }
        queryWrapper.eq("status",'1');
        return super.pageList(queryWrapper,page);
    }
}
