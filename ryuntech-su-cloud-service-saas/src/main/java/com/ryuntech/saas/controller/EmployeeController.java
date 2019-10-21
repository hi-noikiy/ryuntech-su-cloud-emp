package com.ryuntech.saas.controller;

import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;

import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.service.IEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
 * @author liugg 2019年10月21日
 */
@Slf4j
@RestController
@RequestMapping("/employee")
@Api(value = "EmployeeController", tags = {"员工帐号管理"})
public class EmployeeController extends ModuleBaseController {

    @Autowired
    private IEmployeeService iEmployeeService;

    /**
     * 分页查询列表数据，条件查询
     * @param employee
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页、条件查询员工帐号列表")
    @ApiImplicitParam(name = "employee", value = "查询条件", required = true, dataType = "Employee", paramType = "body")
    public Result<IPage<Employee>> list(@RequestBody Employee employee, QueryPage queryPage) {
        String company_id = "1";
        employee.setCompanyId(company_id);
        return  iEmployeeService.selectListBySearch(employee, queryPage);
    }

}
