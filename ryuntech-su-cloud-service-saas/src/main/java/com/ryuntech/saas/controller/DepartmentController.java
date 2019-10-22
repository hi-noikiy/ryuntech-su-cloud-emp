package com.ryuntech.saas.controller;

import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.Department;
import com.ryuntech.saas.api.service.IDepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author liugg 2019年10月21日
 */
@Slf4j
@RestController
@RequestMapping("/department")
@Api(value = "DepartmentController", tags = {"部门管理"})
public class DepartmentController extends ModuleBaseController {

    @Autowired
    private IDepartmentService iDepartmentService;

    /**
     * 分页查询列表数据，条件查询
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询所有部门树")
    public Result list() {
        //todo: 获取当前登录公司ID
        String company_id = "2";
        List<Map> res = iDepartmentService.selectDepartmentTree(company_id,"0", new ArrayList());
        return new Result(res);
    }

    /**
     * 新增部门
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Department department) {
        String company_id = "2";
        log.info(department.toString());
        department.setDepartmentId(String.valueOf(generateId()));
        department.setCompanyId(company_id);
        if (String.valueOf(department.getPid()) != "0") {
            Department parent = iDepartmentService.findById(department.getPid());
            int level = Integer.valueOf(parent.getLevel()) + 1;
            department.setLevel(String.valueOf(level));
        } else {
            department.setLevel("1");
        }
        iDepartmentService.create(department);
        return new Result();
    }

    /**
     * 编辑部门
     * @return
     */
    @PostMapping("/edit")
    public Result edit(@RequestBody Department department) {
        iDepartmentService.update(department);
        return new Result();
    }

    /**
     * 删除部门
     * @return
     */
    @PostMapping("/delete")
    public Result delete(@RequestBody Map p) {
        String id = (String) p.get("id");
        iDepartmentService.removeById(id);
        return new Result();
    }

}
