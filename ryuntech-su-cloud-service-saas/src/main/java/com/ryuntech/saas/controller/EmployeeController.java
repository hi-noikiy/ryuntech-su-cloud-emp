package com.ryuntech.saas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.saas.api.dto.EmployeeDTO;
import com.ryuntech.saas.api.form.EmployeeEditForm;
import com.ryuntech.saas.api.form.EmployeeForm;
import com.ryuntech.saas.api.service.IEmployeeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;


@Slf4j
@RestController
@RequestMapping("/employee")
@Api(value = "EmployeeController", tags = {"员工帐号管理"})
public class EmployeeController extends ModuleBaseController {

    @Autowired
    private IEmployeeService employeeService;

    /**
     * 员工列表
     *
     * @param companyId
     * @param departmentId
     * @param status
     * @param keyWord      模糊匹配 姓名、手机号码
     * @return
     */
    @GetMapping("getPager")
    public Result<IPage<EmployeeDTO>> getPager(
            @RequestParam("companyId") String companyId,
            @RequestParam("departmentId") String departmentId,
            @RequestParam("status") String status,
            @RequestParam("keyWord") String keyWord,
            @RequestParam(value = "pageCode", required = false) String pageCode,
            @RequestParam(value = "pageSize", required = false) String pageSize) {
        if (!StringUtil.isNumber(companyId, 18)) {
            return new Result(CommonEnums.PARAM_ERROR, "公司ID不合法");
        }

        if (!StringUtil.isEmpty(departmentId) && !StringUtil.isNumber(departmentId, 18)) {
            return new Result(CommonEnums.PARAM_ERROR, "部门不合法");
        }

        if (!StringUtil.contains(false, status, "0", "1")) {
            status = null;
        }

        if (keyWord != null && keyWord.length() > 50) {
            return new Result();
        }

        if (!StringUtil.isNumber(pageCode)) {
            pageCode = "1";
        }

        if (!StringUtil.isNumber(pageSize)) {
            pageSize = "10";
        }

        EmployeeForm employeeForm = new EmployeeForm(companyId, departmentId, status, keyWord, Integer.parseInt(pageCode), Integer.parseInt(pageSize));
        try {
            return new Result(employeeService.getPager(employeeForm));
        } catch (Exception e) {
            return new Result(CommonEnums.OPERATE_ERROR, e.getLocalizedMessage());
        }
    }

    @GetMapping("detail")
    public Result detail(
            @RequestParam("employeeId") String employeeId) {
        if (!StringUtil.isNumber(employeeId, 18)) {
            return new Result(CommonEnums.PARAM_ERROR, "员工ID不合法");
        }

        try {
            return new Result(employeeService.detail(employeeId));
        } catch (Exception e) {
            return new Result(CommonEnums.OPERATE_ERROR, e.getLocalizedMessage());
        }
    }

    /**
     * @param companyId
     * @param employeeId
     * @param mobile
     * @param employeeName
     * @param departmentId
     * @param email
     * @param isCharger
     * @param dataType         数据权限（1=本人2=本部门及下属部门3=全部4=指定部门）
     * @param dataDepartmentId 可操作部门ID（多个英文逗号隔开）
     * @param roleIds
     * @return
     */
    @PostMapping("edit")
    public Result edit(
            @RequestParam("companyId") String companyId,
            @RequestParam("employeeId") String employeeId,
            @RequestParam("mobile") String mobile,
            @RequestParam("employeeName") String employeeName,
            @RequestParam("departmentId") String departmentId,
            @RequestParam("email") String email,
            @RequestParam("isCharger") String isCharger,
            @RequestParam("dataType") String dataType,
            @RequestParam("dataDepartmentId") String dataDepartmentId,
            @RequestParam("roleIds") String roleIds) {

        employeeName = StringUtil.trim(employeeName);
        email = StringUtil.trim(email);

        // 新增操作时，companyId有效，employeeId为空
        // 编辑操作时，companyId为空，employeeId有效
        if (StringUtil.isEmpty(employeeId)) {
            if (!StringUtil.isNumber(companyId, 18)) {
                return new Result(CommonEnums.PARAM_ERROR, "公司ID不合法");
            }
            employeeId = null;
        } else {
            if (!StringUtil.isNumber(employeeId, 18)) {
                return new Result(CommonEnums.PARAM_ERROR, "员工ID不合法");
            }
            companyId = null;
        }

        if (!StringUtil.length(employeeName, 1, 50)) {
            return new Result(CommonEnums.PARAM_ERROR, "姓名仅支持1-50个字符");
        }

        if (employeeId == null) {
            if (StringUtil.isMobile(mobile)) {
                return new Result(CommonEnums.PARAM_ERROR, "手机号码不合法");
            }
        }

        if (!StringUtil.isNumber(departmentId, 18)) {
            return new Result(CommonEnums.PARAM_ERROR, "部门ID不合法");
        }

        if (!StringUtil.contains(false, isCharger, "0", "1")) {
            return new Result(CommonEnums.PARAM_ERROR, "是否部门负责人不合法");
        }

        if (!StringUtil.isEmpty(email) && !StringUtil.isMail(email)) {
            return new Result(CommonEnums.PARAM_ERROR, "邮箱格式不正确");
        }

        if (!StringUtil.contains(false, dataType, "1", "2", "3", "4")) {
            return new Result(CommonEnums.PARAM_ERROR, "数据权限不合法");
        }

        if ("4".equals(dataType)) {
            if (dataType == null || !Pattern.matches("[1-9][0-9,]*", dataDepartmentId)) {
                return new Result(CommonEnums.PARAM_ERROR, "数据权限不合法");
            }
        }

        if (!Pattern.matches("[1-9][0-9,]*", roleIds)) {
            return new Result(CommonEnums.PARAM_ERROR, "账号权限不合法");
        }

        EmployeeEditForm employeeEditForm = new EmployeeEditForm(companyId, employeeId, mobile, employeeName, departmentId, email, isCharger, dataType, dataDepartmentId, roleIds);
        try {
            return new Result(employeeService.edit(employeeEditForm));
        } catch (Exception e) {
            return new Result(CommonEnums.OPERATE_ERROR, e.getLocalizedMessage());
        }
    }

    @PostMapping("updateStatus")
    public Result updateStatus(
            @RequestParam("emplyoeeId") String emplyoeeId,
            @RequestParam("status") String status) {
        if (!StringUtil.isNumber(emplyoeeId, 18)) {
            return new Result(CommonEnums.PARAM_ERROR, "员工ID不合法");
        }

        if (!StringUtil.contains(false, status, "0", "1")) {
            return new Result(CommonEnums.PARAM_ERROR, "员工状态不合法");
        }

        try {
            return new Result(employeeService.updateStatus(emplyoeeId, status));
        } catch (Exception e) {
            return new Result(CommonEnums.OPERATE_ERROR, e.getLocalizedMessage());
        }
    }
}
