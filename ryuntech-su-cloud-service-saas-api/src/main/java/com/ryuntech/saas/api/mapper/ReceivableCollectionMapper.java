package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import com.ryuntech.saas.api.model.ReceivableCollection;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 回款表 Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-09-27
 */
public interface ReceivableCollectionMapper extends IBaseMapper<ReceivableCollection> {


    /**
     * 分页查询
     * @param page
     * @param receivableCollection
     * @return
     */
    IPage<ReceivableCollection> selectPageList(@Param("pg") Page<ReceivableCollection> page, @Param("receivableCollection") ReceivableCollection receivableCollection);

}
