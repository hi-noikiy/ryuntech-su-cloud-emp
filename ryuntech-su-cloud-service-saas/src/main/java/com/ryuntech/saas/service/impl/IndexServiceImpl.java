package com.ryuntech.saas.service.impl;

import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.saas.api.dto.IndexDTO;
import com.ryuntech.saas.api.mapper.IndexMapper;
import com.ryuntech.saas.api.model.Index;
import com.ryuntech.saas.api.service.IndexService;
import org.springframework.stereotype.Service;

/**
 * @author EDZ
 */
@Service
public class IndexServiceImpl extends BaseServiceImpl<IndexMapper, Index> implements IndexService {
    @Override
    public Index selectBulletin(IndexDTO indexDTO) {
        return m.selectBulletin(indexDTO);
    }
}
