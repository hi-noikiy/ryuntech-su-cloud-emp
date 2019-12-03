package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.CustomerUserInfoDTO;
import com.ryuntech.saas.api.form.CustomerUserInfoForm;
import com.ryuntech.saas.api.model.CustomerUserInfo;

import java.util.List;
import java.util.Map;

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

    /**
     * 获取用户id和用户名列表，用于搜索选择
     * @param customerUserInfo
     * @return
     */
    List<Map<String, String>> selectCustomerMap(CustomerUserInfo customerUserInfo);

    /**
     * 根据客户对象查询详情信息
     * @param customerUserInfo
     * @return
     */
    CustomerUserInfoDTO selectCustomerUserInfoDTO(CustomerUserInfo customerUserInfo);

    /**
     * 查询客户列表
     * @param customerUserInfoForm
     * @return
     */
    List<CustomerUserInfo> selectByCustomer(CustomerUserInfoForm customerUserInfoForm);

    /**
     * 根据form查询对应的客户对象
     * @param customerUserInfoForm
     * @return
     */
    CustomerUserInfo selectByCustomerF(CustomerUserInfoForm customerUserInfoForm);

    /**
     * 根据当前用户所在公司名称查询公司所有客户名称
     * @param customerName
     * @return
     */
    List<String> customerNameLimit(String customerName);
}
