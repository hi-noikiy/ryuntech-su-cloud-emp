package com.ryuntech.saas.controller;

import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.SysUser;
import com.ryuntech.saas.api.service.ICompanyService;
import com.ryuntech.saas.api.service.SysUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
@Api(value = "CompanyController", tags = {"公司信息接口"})
public class CompanyController extends ModuleBaseController {

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/listBySysUserName/{username}")
    public Result listByEmployeeId(@PathVariable("username") String username) {
        SysUser sysUser = sysUserService.findByName(username);

        try {
            return new Result(companyService.listBySysUserId(sysUser.getSysUserId()));
        } catch (Exception e) {
            return new Result(CommonEnums.OPERATE_ERROR, e.getLocalizedMessage());
        }
    }
}
