package com.ryuntech.saas.controller;

import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.saas.api.service.ICompanyService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
@Api(value = "CompanyController", tags = {"公司信息接口"})
public class CompanyController extends ModuleBaseController {

    @Autowired
    private ICompanyService companyService;

    /**
     * 选择公司，加载当前员工的权限信息
     *
     * @param companyId
     * @return
     */
    @PostMapping("choose")
    public Result choose(
            @RequestParam("companyId") String companyId,
            @RequestParam("sysUserId") String sysUserId) {
        if (!StringUtil.isNumber(companyId, 18)) {
            return new Result(CommonEnums.PARAM_ERROR, "公司ID不合法");
        }

        try {
            return companyService.choose(companyId, sysUserId);
        } catch (Exception e) {
            return new Result(CommonEnums.OPERATE_ERROR, e.getLocalizedMessage());
        }
    }
}
