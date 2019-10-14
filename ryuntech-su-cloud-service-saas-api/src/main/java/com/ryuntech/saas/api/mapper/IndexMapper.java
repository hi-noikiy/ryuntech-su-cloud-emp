package com.ryuntech.saas.api.mapper;

import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.dto.IndexDTO;
import com.ryuntech.saas.api.model.Index;
import com.ryuntech.saas.api.model.ReceivableContract;
import org.apache.ibatis.annotations.Param;

/**
 * @author EDZ
 */
public interface IndexMapper extends IBaseMapper<Index> {


    /**
     * 数据简报统计
     * @param indexDTO
     * @return
     */
    Index selectBulletin(@Param("indexDTO") IndexDTO indexDTO);

}
