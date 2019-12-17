package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.model.BaseDto;
import com.ryuntech.common.model.BaseForm;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.CopyUtil;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.CustomerRiskDTO;
import com.ryuntech.saas.api.form.CustomerRiskForm;
import com.ryuntech.saas.api.form.CustomerUserInfoForm;
import com.ryuntech.saas.api.mapper.CustomerRiskMapper;
import com.ryuntech.saas.api.model.CustomerRisk;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import com.ryuntech.saas.api.service.ICustomerRiskService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 客户风险表 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-11-06
 */
@Service
public class CustomerRiskServiceImpl extends BaseServiceImpl<CustomerRiskMapper, CustomerRisk> implements ICustomerRiskService {

    @Override
    public Result<IPage<CustomerRisk>> pageList(CustomerRiskForm customerRiskForm, QueryPage queryPage) {
        Page<CustomerRisk> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        if (customerRiskForm.getRiskId()!=null) {
            queryWrapper.eq("risk_id", customerRiskForm.getRiskId());
        }
        return super.pageList(queryWrapper,page);
    }

    @Override
    public List<CustomerRiskDTO> selectGroupConcat(CustomerRiskForm customerRiskForm) {
        return baseMapper.selectGroupConcat(customerRiskForm);
    }

    @Override
    public List<String> selectGroupConcatByTime(CustomerRiskForm customerRiskForm) {
        return baseMapper.selectGroupConcatByTime(customerRiskForm);
    }

}
