package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import com.ryuntech.saas.api.model.ReceivableContract;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-09-29
 */
public interface ReceivableCollectionPlanMapper extends IBaseMapper<ReceivableCollectionPlan> {

    /**
     * 分页查询
     * @param page
     * @param receivableCollectionPlan
     * @return
     */
    IPage<ReceivableCollectionPlan> selectPageList(@Param("pg") Page<ReceivableCollectionPlan> page, @Param("receivableCollectionPlan") ReceivableCollectionPlan receivableCollectionPlan);

}
