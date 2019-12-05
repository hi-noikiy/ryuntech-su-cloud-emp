package com.ryuntech.saas.controller;

import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.saas.api.model.SysUser;
import com.ryuntech.saas.api.service.ICompanyService;
import com.ryuntech.saas.api.service.SysUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@Api(value = "CompanyController", tags = {"公司信息接口"})
public class CompanyController extends ModuleBaseController {

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/listBySysUserName/{username}")
    public Result listBySysUserName(@PathVariable("username") String username) {
        SysUser sysUser = sysUserService.findByName(username);

        try {
            return new Result(companyService.listBySysUserId(sysUser.getSysUserId()));
        } catch (Exception e) {
            return new Result(CommonEnums.OPERATE_ERROR, e.getLocalizedMessage());
        }
    }

    @GetMapping("listBySysUserId")
    public Result listByEmployeeId(
            @RequestParam("sysUserId") String sysUserId) {
        if (!StringUtil.isNumber(sysUserId, 18)) {
            return new Result(CommonEnums.PARAM_ERROR, "用户ID不合法");
        }

        try {
            return new Result(companyService.listBySysUserId(sysUserId));
        } catch (Exception e) {
            return new Result(CommonEnums.OPERATE_ERROR, e.getLocalizedMessage());
        }
    }

    /**
     * 选择公司，加载当前员工的权限信息
     *
     * @param companyId
     * @return
     */
    @PostMapping("choose")
    public Result choose(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
                         @RequestParam("companyId") String companyId) {
        if (!StringUtil.isNumber(companyId, 18)) {
            return new Result(CommonEnums.PARAM_ERROR, "公司ID不合法");
        }

        String token = authHeader.replace("Bearer", "").trim();

        try {
            return new Result(companyService.choose(companyId, token));
        } catch (Exception e) {
            return new Result(CommonEnums.OPERATE_ERROR, e.getLocalizedMessage());
        }
    }
}
