package com.ryuntech.saas.api.service;

import com.ryuntech.saas.api.model.Company;
import org.springframework.web.bind.annotation.RequestParam;
import com.ryuntech.common.utils.Result;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author antu
 * @since 2019-10-17
 */
public interface ICompanyService extends IBaseService<Company> {
    Company selectByCompany(Company company);

   Result choose(
            @RequestParam("companyId") String companyId,
            @RequestParam("sysUserId") String sysUserId) throws Exception;
}
