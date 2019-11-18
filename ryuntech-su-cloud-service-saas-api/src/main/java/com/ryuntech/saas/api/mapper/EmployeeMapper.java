package com.ryuntech.saas.api.mapper;

import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.model.Employee;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-10-15
 */
public interface EmployeeMapper extends IBaseMapper<Employee> {

    /**
     * 根据用户id查询该用户所在的公司
     * @param userId
     * @return
     */
    List<Map<String, String>> selectCompanys(String userId);

}
