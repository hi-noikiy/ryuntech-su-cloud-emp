package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.constant.generator.IncrementIdGenerator;
import com.ryuntech.common.constant.generator.UniqueIdGenerator;
import com.ryuntech.common.exception.RyunBizException;
import com.ryuntech.common.model.CurrentUser;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.common.utils.SystemTool;
import com.ryuntech.common.utils.redis.JedisUtil;
import com.ryuntech.saas.api.dto.*;
import com.ryuntech.saas.api.form.RoleForm;
import com.ryuntech.saas.api.mapper.SysPermMapper;
import com.ryuntech.saas.api.mapper.SysRoleMapper;
import com.ryuntech.saas.api.mapper.SysRolePermMapper;
import com.ryuntech.saas.api.mapper.SysUserRoleMapper;
import com.ryuntech.saas.api.model.SysPerm;
import com.ryuntech.saas.api.model.SysRole;
import com.ryuntech.saas.api.model.SysRolePerm;
import com.ryuntech.saas.api.model.SysUserRole;
import com.ryuntech.saas.api.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-09-12
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRolePermMapper sysRolePermMapper;

    @Autowired
    private SysPermMapper sysPermMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private JedisUtil jedisUtil;

    @Override
    public Result<IPage<SysRole>> pageList(SysRole sysRole, QueryPage queryPage) {
        Page<SysRole> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        if (sysRole.getRname() != null) {
            queryWrapper.eq("rname", sysRole.getRname());
        }
        return super.pageList(queryWrapper, page);
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
        return re == null ? false : re.booleanValue();
    }

    @Override
    public List<RoleInfoDTO> getRoleInfoList() {
        CurrentUser employee = SystemTool.currentUser(jedisUtil);
        if (employee == null) {
            throw new RyunBizException("系统错误, 无法获取当前操作用户信息");
        }
        String companyId = employee.getCompanyId();

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
        CurrentUser employee = SystemTool.currentUser(jedisUtil);
        if (employee == null) {
            throw new RyunBizException("系统错误, 无法获取当前操作用户信息");
        }
        String companyId = employee.getCompanyId();
        return baseMapper.getRoleDetailByCompanyIdAndRoleId(companyId, roleId);
    }

    @Override
    public List<RoleNameDTO> getNameList() {
        CurrentUser employee = SystemTool.currentUser(jedisUtil);
        if (employee == null) {
            throw new RyunBizException("系统错误, 无法获取当前操作用户信息");
        }
        String companyId = employee.getCompanyId();
        return baseMapper.getNameList(companyId);
    }

    @Override
    public void edit(RoleForm roleForm) {
        CurrentUser employee = SystemTool.currentUser(jedisUtil);
        if (employee == null) {
            throw new RyunBizException("系统错误, 无法获取当前操作用户信息");
        }
        String empId = employee.getEmployeeId();
        String companyId = employee.getCompanyId();

        // 查询同名岗位
        SysRole sameNameRole = baseMapper.selectOne(
                new QueryWrapper<SysRole>().eq("RNAME", roleForm.getRoleName()).eq("COMPANY_ID", companyId));
        // 检测同名角色是否存在(存在同名角色, 且角色id不同)
        if (sameNameRole != null && !sameNameRole.getRid().equals(roleForm.getRoleId())) {
            throw new RyunBizException("已经存在同名的角色, 请指定新的角色名.");
        }

        // 检查权限是否存在
        List<String> newPermIdList = roleForm.getPermList();
        List<SysPerm> existedPermList = sysPermMapper.queryByIdListAndCompanyId(newPermIdList);
        if (existedPermList.size() != newPermIdList.size()) {
            throw new RyunBizException("您所选择的角色权限有误, 请重新为角色指定权限.");
        }

        // 数据库实体
        SysRole newRole = roleForm.convertToSysRole();
        List<SysRolePerm> rolePermList;

        if (!StringUtil.isEmpty(newRole.getRid())) {
            // 修改角色
            String roleId = newRole.getRid();
            // 获取旧角色并检验
            SysRole oldRole = baseMapper.selectOne(new QueryWrapper<SysRole>().eq("RID", roleId).eq("COMPANY_ID", companyId));
            if (oldRole == null) {
                throw new RyunBizException("角色不存在, 操作失败");
            }
            if (oldRole.getIsAdmin() == 1) {
                throw new RyunBizException("管理员角色不允许编辑, 操作失败");
            }

            // 更新角色
            baseMapper.update(newRole, new QueryWrapper<SysRole>().eq("RID", roleId).eq("COMPANY_ID", companyId));

            // 更新权限 删除旧权限, 重新插入
            sysRolePermMapper.delete(new QueryWrapper<SysRolePerm>().eq("ROLE_ID", roleId));
            rolePermList = roleForm.convertToSysRolePermList();
            if (rolePermList.size() > 0) {
                sysRolePermMapper.batchInsert(rolePermList);
            }
            log.info("员工【{}】修改了角色详情：{}", empId, newRole);
        } else {
            // 新增角色, 新建一个 roleId
            UniqueIdGenerator uniqueIdGenerator = UniqueIdGenerator.getInstance(IncrementIdGenerator.getServiceId());
            String roleId = uniqueIdGenerator.nextStrId();
            // 完善角色对象的数据并插入
            newRole.setRid(roleId);
            newRole.setIsAdmin(0);
            newRole.setIsPreset(0);
            newRole.setCompanyId(companyId);
            newRole.setCreated(newRole.getUpdated());
            baseMapper.insert(newRole);

            // 插入角色权限
            roleForm.setRoleId(roleId);
            rolePermList = roleForm.convertToSysRolePermList();
            if (rolePermList.size() > 0) {
                sysRolePermMapper.batchInsert(rolePermList);
            }
            log.info("员工【{}】创建了角色：{}", empId, newRole);
        }
    }

    @Override
    public void delete(String roleId) {
        CurrentUser employee = SystemTool.currentUser(jedisUtil);
        if (employee == null) {
            throw new RyunBizException("系统错误, 无法获取当前操作用户信息");
        }
        String empId = employee.getEmployeeId();
        String companyId = employee.getCompanyId();

        // 获取旧角色并检验
        SysRole oldRole = baseMapper.selectOne(new QueryWrapper<SysRole>().eq("RID", roleId).eq("COMPANY_ID", companyId));
        if (oldRole == null) {
            throw new RyunBizException("角色不存在, 操作失败");
        }
        if (oldRole.getIsAdmin() == 1) {
            throw new RyunBizException("管理员角色不允许删除, 操作失败");
        }
        // 检查角色下有没有员工
        List<SysUserRole> roleEmpList = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("ROLE_ID", roleId));
        if (roleEmpList.size() > 0) {
            throw new RyunBizException("该角色下有关联员工, 请先为员工分配新角色. ");
        }
        // 删除角色, 以及角色关联的权限
        baseMapper.deleteById(roleId);
        sysRolePermMapper.delete(new QueryWrapper<SysRolePerm>().eq("ROLE_ID", roleId));
        log.info("员工【{}】删除了角色：{}", empId, oldRole);
    }
}
