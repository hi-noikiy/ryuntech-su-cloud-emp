package com.ryuntech.saas.controller;


import com.ryuntech.common.constant.PermInfo;
import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.PermGroupDTO;
import com.ryuntech.saas.api.dto.RoleDetailDTO;
import com.ryuntech.saas.api.dto.RoleNameDTO;
import com.ryuntech.saas.api.form.RoleForm;
import com.ryuntech.saas.api.service.ISysRoleService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@PermInfo(value = "系统角色模块")
@RestController
@RequestMapping("/role")
@Api(value = "SysRoleController", tags = {"用户角色管理接口"})
public class SysRoleController extends ModuleBaseController {

    @Autowired
    private ISysRoleService roleService;

    /**
     * 获取角色名称列表, 编辑员工时用
     * @return 角色名称与id 的列表
     */
    @GetMapping("nameList")
    public Result<List<RoleNameDTO>> nameList() {
        return new Result<>(roleService.getNameList());
    }

    /**
     * 获取角色列表, 含各角色拥有的资源名称
     * @return 角色列表(含资源名称)
     */
    @GetMapping("list")
    public Result<List> getList(){
        return new Result<>(roleService.getRoleInfoList());
    }

    /**
     * 获取当前角色的详情, 包括资源的名称与id
     * @return 角色详情(拥有资源的列表)
     */
    @GetMapping("detail")
    public Result<RoleDetailDTO> getDetail(String roleId) {
        RoleDetailDTO roleDetail = roleService.getRoleDetail(roleId);
        if (roleDetail != null) {
            return new Result<>(roleDetail);
        }
        return new Result<>(CommonEnums.PARAM_ERROR, "未找到对应的角色详情, 请重新选择角色");
    }

    /**
     * 获取系统中所有资源
     * @return 所有资源, 按所属组分成多个list
     */
    @GetMapping("allResources")
    public Result<List<PermGroupDTO>> getAllResources() {
        return new Result<>(roleService.getAllResources());
    }

    /**
     * 编辑角色, 包含新增与修改
     * @return 操作结果(成功 or 失败)
     */
    @PostMapping("edit")
    public Result edit(RoleForm roleForm) {
        try {
            roleService.edit(roleForm);
            return new Result();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(CommonEnums.OPERATE_ERROR, e.getMessage());
        }
    }

    /**
     * 删除角色
     * @return 操作结果(成功 or 失败)
     */
    @PostMapping("delete")
    public Result delete(String roleId){
        try {
            roleService.delete(roleId);
            return new Result();
        } catch (Exception e) {
            log.error("删除角色发生异常", e);
            return new Result(CommonEnums.OPERATE_ERROR, e.getMessage());
        }
    }
}
