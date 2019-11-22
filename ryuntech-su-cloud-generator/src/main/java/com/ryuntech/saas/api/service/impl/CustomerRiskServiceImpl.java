package com.ryuntech.saas.api.service.impl;

import com.ryuntech.saas.api.entity.CustomerRisk;
import com.ryuntech.saas.api.mapper.CustomerRiskMapper;
import com.ryuntech.saas.api.service.ICustomerRiskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户风险表 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-11-21
 */
@Service
public class CustomerRiskServiceImpl extends ServiceImpl<CustomerRiskMapper, CustomerRisk> implements ICustomerRiskService {

}
