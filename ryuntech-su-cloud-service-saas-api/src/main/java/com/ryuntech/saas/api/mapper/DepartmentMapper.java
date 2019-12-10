package com.ryuntech.saas.api.mapper;

import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.dto.DepartmetnTreeNodeDTO;
import com.ryuntech.saas.api.model.Department;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-10-15
 */
@Component
public interface DepartmentMapper extends IBaseMapper<Department> {

    List<DepartmetnTreeNodeDTO> getDepartmentTreeByCompanyId(@Param("companyId") String companyId);


    List<DepartmetnTreeNodeDTO> getChildren(@Param("companyId") String companyId, @Param("pId") String pId);

}
