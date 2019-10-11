package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import com.ryuntech.saas.api.model.ReceivableContract;

/**
 * <p>
 * 客户信息表 服务类
 * </p>
 *
 * @author antu
 * @since 2019-09-29
 */
public interface ICustomerUserInfoService extends IBaseService<CustomerUserInfo> {

    /**
     * 分页查询
     * @param customerUserInfo
     * @param queryPage
     * @return
     */
    Result<IPage<CustomerUserInfo>> pageList(CustomerUserInfo customerUserInfo, QueryPage queryPage);

    /**
     * 分页查询
     * @param customerUserInfo
     * @param queryPage
     * @return
     */
    Result<IPage<CustomerUserInfo>> selectPageList(CustomerUserInfo customerUserInfo, QueryPage queryPage);
}
