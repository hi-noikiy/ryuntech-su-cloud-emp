package com.ryuntech.saas.service.impl;

import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.saas.api.dto.LoginConpanyDTO;
import com.ryuntech.saas.api.mapper.CompanyMapper;
import com.ryuntech.saas.api.model.Company;
import com.ryuntech.saas.api.service.ICompanyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-10-17
 */
@Service
public class CompanyServiceImpl extends BaseServiceImpl<CompanyMapper, Company> implements ICompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public Company selectByCompany(Company company) {
        if (StringUtils.isNotBlank(company.getName())) {
            queryWrapper.eq("name", company.getName());
        }
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<LoginConpanyDTO> listBySysUserId(String sysUserId) {
        return companyMapper.listBySysUserId(sysUserId);
    }
}
