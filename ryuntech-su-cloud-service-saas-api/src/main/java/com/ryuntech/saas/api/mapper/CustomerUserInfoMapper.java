package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * @param customerUserInfo
     * @return
     */
    IPage<CustomerUserInfo> selectPageList(@Param("pg") Page<CustomerUserInfo> page, @Param("customerUserInfo") CustomerUserInfo customerUserInfo);

    /**
     * 获取用户id和客户名称列表，用于搜索选择
     * @param customerUserInfo
     * @return
     */
    List<CustomerUserInfo> selectCustomerMap(@Param("customerUserInfo") CustomerUserInfo customerUserInfo);
}
