package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.saas.api.mapper.CustomerUserInfoMapper;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import com.ryuntech.saas.api.service.ICustomerUserInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户信息表 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-09-29
 */
@Service
public class CustomerUserInfoServiceImpl extends BaseServiceImpl<CustomerUserInfoMapper, CustomerUserInfo> implements ICustomerUserInfoService {

}
