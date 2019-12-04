package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.model.SysRole;
import com.ryuntech.saas.api.model.SysRolePerm;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-09-12
 */
@Component
public interface SysRolePermMapper extends IBaseMapper<SysRolePerm> {

}
