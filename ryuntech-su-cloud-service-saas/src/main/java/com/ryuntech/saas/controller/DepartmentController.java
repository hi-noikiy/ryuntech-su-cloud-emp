package com.ryuntech.saas.controller;

import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.saas.api.dto.DepartmetnTreeNodeDTO;
import com.ryuntech.saas.api.form.DepartmentForm;
import com.ryuntech.saas.api.service.IDepartmentService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/department")
@Api(value = "DepartmentController", tags = {"部门管理"})
public class DepartmentController extends ModuleBaseController {

    @Autowired
    private IDepartmentService departmentService;

    /**
     * 获取部门结构树
     */
    @GetMapping("tree")
    public Result<List<DepartmetnTreeNodeDTO>> getDepartmentTree(){
        List<DepartmetnTreeNodeDTO> deptTree = departmentService.getDepartmentTree();
        return new Result<>(deptTree);
    }

    /**
     * 编辑部门(新增/修改)
     * @param form 新的部门信息
     * @return 操作结果
     */
    @PostMapping("edit")
    public Result edit(DepartmentForm form){
        try {
            if (StringUtil.isEmpty(form.getDeptName())) {
                return new Result(CommonEnums.OPERATE_ERROR, "部门名称不能为空!");
            }
            departmentService.edit(form);
            return new Result();
        } catch (Exception e) {
            log.error("编辑部门发生异常", e);
            return new Result(CommonEnums.OPERATE_ERROR, e.getMessage());
        }
    }

    /**
     * 删除部门
     * @param deptId 要删除的部门id
     * @return 操作结果
     */
    @PostMapping("delete")
    public Result delete(String deptId){
        try {
            departmentService.delete(deptId);
            return new Result();
        } catch (Exception e) {
            log.error("删除部门发生异常", e);
            return new Result(CommonEnums.OPERATE_ERROR, e.getMessage());
        }
    }

    /**
     * 将一个部门的员工迁移到另一个部门
     * @param oldDeptId 旧部门id
     * @param newDeptId 新部门id
     * @return 操作结果, 成功的记录数
     */
    @PostMapping("migrate")
    public Result<Integer> migrateToAnotherDept(String oldDeptId, String newDeptId){
        try {
            return new Result<>(departmentService.migrateToAnotherDept(oldDeptId, newDeptId));
        } catch (Exception e) {
            log.error("删除部门发生异常", e);
            return new Result<>(CommonEnums.OPERATE_ERROR, e.getMessage());
        }
    }
}
