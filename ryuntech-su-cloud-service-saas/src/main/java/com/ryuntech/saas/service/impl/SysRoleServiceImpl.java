package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.PermGroupDTO;
import com.ryuntech.saas.api.dto.RoleDetailDTO;
import com.ryuntech.saas.api.dto.RoleInfoDTO;
import com.ryuntech.saas.api.mapper.SysRoleMapper;
import com.ryuntech.saas.api.model.SysRole;
import com.ryuntech.saas.api.service.ISysRoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-09-12
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Override
    public Result<IPage<SysRole>> pageList(SysRole sysRole, QueryPage queryPage) {
        Page<SysRole> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        if (sysRole.getRname()!=null) {
            queryWrapper.eq("rname", sysRole.getRname());
        }
        return super.pageList(queryWrapper,page);
    }


    @Override
    public List<String> getRoleIdsByUserId(String userId) {
        return baseMapper.getRoleIdsByUserId(userId);
    }

    @Override
    public boolean checkRidsContainRval(List<String> rids, String rval) {
        if (rids.isEmpty()) {
            return false;
        }
        Boolean re = baseMapper.checkRidsContainRval(rids, rval);
        return re==null?false:re.booleanValue();
    }

    @Override
    public List<RoleInfoDTO> getRoleInfoList() {
        // todo 获取当前员工的公司id;
        String companyId = "773031356912366360";
        List<RoleDetailDTO> roleDetailList = baseMapper.getRoleDetailList(companyId);
        List<RoleInfoDTO> roleInfoList = new ArrayList<>();
        for (RoleDetailDTO detail : roleDetailList) {
            roleInfoList.add(new RoleInfoDTO(detail));
        }
        return roleInfoList;
    }

    @Override
    public List<PermGroupDTO> getAllResources() {
        return baseMapper.getPermGroup();
    }

    @Override
    public RoleDetailDTO getRoleDetail(String roleId) {
        // todo 获取当前员工的公司id;
        String companyId = "773031356912366360";
        return baseMapper.getRoleDetailByCompanyIdAndRoleId(companyId, roleId);
    }
}
