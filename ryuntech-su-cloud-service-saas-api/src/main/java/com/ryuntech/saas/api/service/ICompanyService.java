package com.ryuntech.saas.api.service;

import com.ryuntech.saas.api.model.Company;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author antu
 * @since 2019-10-17
 */
public interface ICompanyService extends IBaseService<Company> {

    /**
     * 根据指定对象查询公司名
     * @param company
     * @return
     */
    Company selectByCompany(Company company);
}
