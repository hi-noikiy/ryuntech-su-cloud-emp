package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.saas.api.mapper.CompanyConfigMapper;
import com.ryuntech.saas.api.model.CompanyConfig;
import com.ryuntech.saas.api.service.ICompanyConfigService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 公司系统设置表 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-10-17
 */
@Service
public class CompanyConfigServiceImpl extends BaseServiceImpl<CompanyConfigMapper, CompanyConfig> implements ICompanyConfigService {

}
