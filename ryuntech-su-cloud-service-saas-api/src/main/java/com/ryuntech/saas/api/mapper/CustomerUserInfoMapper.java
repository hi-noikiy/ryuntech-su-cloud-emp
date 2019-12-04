package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.form.CustomerUserInfoForm;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户信息表 Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-09-29
 */
public interface CustomerUserInfoMapper extends IBaseMapper<CustomerUserInfo> {

    /**
     * 分页查询
     * @param page
     * @param customerUserInfoForm
     * @return
     */
    IPage<CustomerUserInfo> selectPageList(@Param("pg") Page<CustomerUserInfo> page, @Param("customerUserInfoForm") CustomerUserInfoForm customerUserInfoForm);

    /**
     * 获取用户id和客户名称列表，用于搜索选择
     * @param customerUserInfo
     * @return
     */
    List<Map<String, String>> selectCustomerMap(@Param("customerUserInfo") CustomerUserInfo customerUserInfo);

    /**
     * 总待收
     * @param customerUserInfo
     * @return
     */
    String selectAllBalanceAmounts(@Param("customerUserInfo") CustomerUserInfo customerUserInfo);


    /**
     * 总已回款
     * @param customerUserInfo
     * @return
     */
    String selectAllCollectionAmount(@Param("customerUserInfo") CustomerUserInfo customerUserInfo);

    /**
     * 总合同金额
     * @param customerUserInfo
     * @return
     */
    String selectAllContractAmount(@Param("customerUserInfo") CustomerUserInfo customerUserInfo);

    /**
     *根据当前用户所在公司名称查询公司所有客户名称
     * @param customerName
     * @return
     */
    List<String> customerNameLimit(String customerName);
}
