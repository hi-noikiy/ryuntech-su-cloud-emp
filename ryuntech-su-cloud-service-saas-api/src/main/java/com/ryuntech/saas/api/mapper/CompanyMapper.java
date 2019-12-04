package com.ryuntech.saas.api.mapper;

import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.dto.LoginConpanyDTO;
import com.ryuntech.saas.api.model.Company;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-10-17
 */
@Component
public interface CompanyMapper extends IBaseMapper<Company> {

    List<LoginConpanyDTO> listBySysUserId(String sysUserId);
}
