package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.saas.api.mapper.CustomerRiskMapper;
import com.ryuntech.saas.api.model.CustomerRisk;
import com.ryuntech.saas.api.service.ICustomerRiskService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户风险表 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-11-06
 */
@Service
public class CustomerRiskServiceImpl extends BaseServiceImpl<CustomerRiskMapper, CustomerRisk> implements ICustomerRiskService {

}
