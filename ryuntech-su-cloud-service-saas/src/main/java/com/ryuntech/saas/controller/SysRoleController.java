package com.ryuntech.saas.controller;


import com.ryuntech.common.constant.PermInfo;
import com.ryuntech.saas.api.service.ISysRoleService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@PermInfo(value = "系统角色模块")
@RestController
@RequestMapping("/role")
@Api(value = "SysRoleController", tags = {"用户角色管理接口"})
public class SysRoleController extends ModuleBaseController {
    @Autowired
    private ISysRoleService roleService;
}
