package com.ryuntech.saas.service.impl;

import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.saas.api.dto.IndexDTO;
import com.ryuntech.saas.api.dto.WeChatIndexDTO;
import com.ryuntech.saas.api.form.ReceivableContractForm;
import com.ryuntech.saas.api.form.WeChatIndexDetailForm;
import com.ryuntech.saas.api.mapper.IndexMapper;
import com.ryuntech.saas.api.model.Index;
import com.ryuntech.saas.api.model.ReceivableContract;
import com.ryuntech.saas.api.service.IndexService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author EDZ
 */
@Service
public class IndexServiceImpl extends BaseServiceImpl<IndexMapper, Index> implements IndexService {
    @Override
    public Index selectBulletin(IndexDTO indexDTO) {
        return m.selectBulletin(indexDTO);
    }

    @Override
    public String selectBalanceAmounts(WeChatIndexDTO weChatIndexDTO) {
        return baseMapper.selectBalanceAmounts(weChatIndexDTO);
    }

    @Override
    public String selectContractAmounts(WeChatIndexDTO weChatIndexDTO) {
        return baseMapper.selectContractAmounts(weChatIndexDTO);
    }

    @Override
    public String selectCollectionAmounts(WeChatIndexDTO weChatIndexDTO) {
        return baseMapper.selectCollectionAmounts(weChatIndexDTO);
    }

    @Override
    public Integer selectOverdueNumber(WeChatIndexDTO weChatIndexDTO) {
        return baseMapper.selectOverdueNumber(weChatIndexDTO);
    }

    @Override
    public String selectOverdueSum(WeChatIndexDTO weChatIndexDTO) {
        return baseMapper.selectOverdueSum(weChatIndexDTO);
    }

    @Override
    public Integer selectExpireNumber(WeChatIndexDTO weChatIndexDTO) {
        return baseMapper.selectExpireNumber(weChatIndexDTO);
    }

    @Override
    public String selectExpireSum(WeChatIndexDTO weChatIndexDTO) {
        return baseMapper.selectExpireSum(weChatIndexDTO);
    }

    @Override
    public Integer totalContractNumber(WeChatIndexDetailForm weChatIndexDetailForm) {
        return baseMapper.totalContractNumber(weChatIndexDetailForm);
    }

    @Override
    public Integer totalCustomerNumber(WeChatIndexDetailForm weChatIndexDetailForm) {
        return baseMapper.totalCustomerNumber(weChatIndexDetailForm);
    }

    @Override
    public List<ReceivableContract> totalReceivableSum(WeChatIndexDetailForm weChatIndexDetailForm) {
        return baseMapper.totalReceivableSum(weChatIndexDetailForm);
    }


}
