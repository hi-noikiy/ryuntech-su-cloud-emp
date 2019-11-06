package com.ryuntech.saas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.Department;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.model.SysUser;
import com.ryuntech.saas.api.model.UserRoleLimit;
import com.ryuntech.saas.api.service.IDepartmentService;
import com.ryuntech.saas.api.service.IEmployeeService;
import com.ryuntech.saas.api.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private SysUserService sysUserService;

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

    /**
     * 更新员工状态 0-正常 1-禁用
     * @return
     */
    @PostMapping("updateStatus")
    @ApiOperation(value = "更新员工状态")
    @ApiImplicitParam(name = "param", value = "更新参数", required = true, dataType = "Map", paramType = "body")
    public Result updateStatus(@RequestBody Map param) {
        if (!param.containsKey("employeeId")) {
            return new Result(CommonEnums.PARAM_ERROR,"员工ID不能为空");
        }
        Employee employee = new Employee();
        employee.setEmployeeId((String)param.get("employeeId"));
        employee.setStatus((Integer)param.get("status"));
        iEmployeeService.updateById(employee);
        return new Result();
    }

    /**
     * 删除员工
     * @return
     */
    @PostMapping("del")
    @ApiOperation(value = "删除员工")
    @ApiImplicitParam(name = "param", value = "员工ID", required = true, dataType = "Map", paramType = "body")
    public Result del(@RequestBody Map param) {
        if (!param.containsKey("employeeId")) {
            return new Result(CommonEnums.PARAM_ERROR,"员工ID不能为空");
        }
        String employeeId = (String) param.get("employeeId");
        iEmployeeService.removeById(employeeId);
        return new Result();
    }


    /**
     * 添加员工
     * @return
     */
    @PostMapping("add")
    @ApiOperation(value = "添加员工")
    @ApiImplicitParam(name = "employee", value = "员工ID", required = true, dataType = "Map", paramType = "body")
    public Result del(@RequestBody Employee employee) {
        employee.setEmployeeId(String.valueOf(generateId()));
        employee.setStatus(0);
        iEmployeeService.save(employee);
        return new Result();
    }

    /**
     * 编辑部门
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestBody Employee employee) {
        iEmployeeService.updateById(employee);
        return new Result();
    }

    /**
     * 根据用户名获取员工id查询该用户所在的公司的角色及权限
     * @param username
     * @return
     */
    @GetMapping("/companylist/{username}")
    @ApiOperation(value = "根据用户名获取员工id查询该用户所在的公司")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    public Result<List<UserRoleLimit>> userRolesLimits(@PathVariable("username") String username) {
        SysUser user = sysUserService.findByName(username);
        String userId = user.getId();

        // 用户所在公司角色权限信息
        List<Map<String, String>> companys = iEmployeeService.selectCompanys(userId);

        // 获取公司集合
        Set<String> companySet = new HashSet<>();
        for(Map company : companys) {
            companySet.add((String) company.get("companyName"));
        }

        // 获取公司相应角色
        Set<String> roleSet = new HashSet<>();
        List<Map<String, Set<String>>> listm = new ArrayList<>();
        Map<String, Set<String>> mm = new HashMap<>();
        for(String cs : companySet) {
            for(Map<String, String> company : companys) {
                if(cs.equals(company.get("companyName"))) {
                    if(company.get("rval") != null) {
                        roleSet.add((String) company.get("rval"));
                    }
                }
            }
            mm.put(cs, roleSet);
        }
        listm.add(mm);

        // 获取公司相应权限
        Set<String> roleSet1 = new HashSet<>();
        List<Map<String, Set<String>>> listm1 = new ArrayList<>();
        Map<String, Set<String>> mm1 = new HashMap<>();
        for(String cs : companySet) {
            for(Map<String, String> company : companys) {
                if(cs.equals(company.get("companyName"))) {
                    if(company.get("permVal") != null) {
                        roleSet1.add((String) company.get("permVal"));
                    }
                }
            }
            mm1.put(cs, roleSet1);
        }
        listm1.add(mm1);

        // 公司角色权限整合
        List<UserRoleLimit> listUserrl = new ArrayList<>();
        UserRoleLimit userRoleLimit = null;


        for(String cs : companySet) {
            userRoleLimit = new UserRoleLimit();
            userRoleLimit.setCompany(cs);
            for(Map<String, Set<String>> ms : listm) {
                for(String key : ms.keySet()) {
                    if (key.equals(cs)) {
                        userRoleLimit.setUserRoles(ms.get(cs));
                    }
                }
            }
            for(Map<String, Set<String>> ms : listm1) {
                for(String key : ms.keySet()) {
                    if (key.equals(cs)) {
                        userRoleLimit.setUserLimit(ms.get(cs));
                    }
                }
            }
            listUserrl.add(userRoleLimit);
        }

        return new Result<>(listUserrl);
    }

}
