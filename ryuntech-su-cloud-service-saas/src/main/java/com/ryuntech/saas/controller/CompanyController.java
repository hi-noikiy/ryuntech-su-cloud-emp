package com.ryuntech.saas.controller;

import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.saas.api.service.ICompanyService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
@Api(value = "CompanyController", tags = {"公司信息接口"})
public class CompanyController extends ModuleBaseController {

    @Autowired
    private ICompanyService companyService;

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
}
