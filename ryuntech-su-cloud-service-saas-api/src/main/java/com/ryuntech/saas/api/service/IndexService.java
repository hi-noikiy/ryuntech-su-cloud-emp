package com.ryuntech.saas.api.service;

import com.ryuntech.saas.api.dto.IndexDTO;
import com.ryuntech.saas.api.model.FinanceUserInfo;
import com.ryuntech.saas.api.model.Index;
import com.ryuntech.saas.api.model.ReceivableContract;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author EDZ
 */
public interface IndexService extends IBaseService<Index> {
    /**
     * 数据简报统计
     * @param indexDTO
     * @return
     */
    Index selectBulletin( IndexDTO indexDTO);
}
