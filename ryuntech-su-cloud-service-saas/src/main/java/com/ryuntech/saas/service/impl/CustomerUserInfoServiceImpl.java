package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
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

    @Override
    public Result<IPage<CustomerUserInfo>> pageList(CustomerUserInfo customerUserInfo, QueryPage queryPage) {
        Page<CustomerUserInfo> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        if (customerUserInfo.getCustomerId()!=null) {
            queryWrapper.eq("customer_id", customerUserInfo.getCustomerId());
        }
        return super.pageList(queryWrapper,page);
    }

    @Override
    public Result<IPage<CustomerUserInfo>> selectPageList(CustomerUserInfo customerUserInfo, QueryPage queryPage) {
        Page<CustomerUserInfo> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        return new Result(m.selectPageList(page,customerUserInfo));
    }


}
