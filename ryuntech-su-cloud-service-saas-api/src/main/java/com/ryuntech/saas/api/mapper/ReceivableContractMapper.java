package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.dto.ReceivableContractDTO;
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
    /**
     * 分页查询合同
     * @param page
     * @param receivableContract
     * @return
     */
    IPage<ReceivableContract> selectPageList(@Param("pg") Page<ReceivableContract> page, @Param("receivableContract") ReceivableContract receivableContract);

    /**
     *
     * @param receivableContractDTO
     * @return
     */
    String selectSumByRContract(@Param("receivableContractDTO") ReceivableContractDTO receivableContractDTO);

}
