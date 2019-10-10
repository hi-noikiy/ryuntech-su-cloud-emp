package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.model.PaymentResult;
import com.ryuntech.saas.api.model.ReceivableContract;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 应收合同表 Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-09-27
 */
public interface ReceivableContractMapper extends IBaseMapper<ReceivableContract> {
    IPage<ReceivableContract> selectPageList(@Param("pg") Page<ReceivableContract> page, @Param("receivableContract") ReceivableContract receivableContract);

}
