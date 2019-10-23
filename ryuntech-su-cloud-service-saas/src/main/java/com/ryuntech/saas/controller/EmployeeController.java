package com.ryuntech.saas.controller;

import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;

import com.ryuntech.saas.api.model.Department;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.service.IDepartmentService;
import com.ryuntech.saas.api.service.IEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.*;


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
    @Autowired
    private IDepartmentService iDepartmentService;

    /**
     * 分页查询列表数据，条件查询
     * @param param
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页、条件查询员工帐号列表")
    @ApiImplicitParam(name = "param", value = "查询条件", required = true, dataType = "Map", paramType = "body")
    public Result<IPage<Employee>> list(@RequestBody Map param, QueryPage queryPage) {
        //todo: 获取当前登录公司ID
        String company_id = "1";
//        param.put("companyId",company_id);
        IPage<Employee> iPage = iEmployeeService.selectListBySearch(param, queryPage);
        List<Employee> employeeList = iPage.getRecords();
        List departmentIds = new ArrayList();
        for (Employee employee : employeeList) {
            departmentIds.add(employee.getDepartmentId());
        }
        // todo 角色 数据权限
        Collection<Department> departmentList = iDepartmentService.listByIds(departmentIds);
        if (!departmentList.isEmpty()) {
            Map<String,Department> departmentMap = new HashMap();
            for (Department department : departmentList) {
                departmentMap.put(department.getDepartmentId(),department);
            }
            for (Employee employee : employeeList) {
                String deName = departmentMap.get(employee.getDepartmentId()).getDepartmentName();
                employee.setPropertys("departmentName",deName);
            }
        }
        return  new Result<>(iPage);
    }

}
