package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.saas.api.mapper.DepartmentMapper;
import com.ryuntech.saas.api.model.Department;
import com.ryuntech.saas.api.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-10-15
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<DepartmentMapper, Department> implements IDepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Override

    /*
    * 返回部门组织树
        {
       department_id: 2,
       department_name: '部门01',
       level: '级别',
       pid: '1',
       children: [
         {},{}
       ]}
         */
    public List<Map> selectDepartmentTree(String company_id, String pid, ArrayList path) {
        List<Department> departments = departmentMapper.selectList(new QueryWrapper<Department>().eq("company_id", company_id).eq("pid",pid));
        List<Map> res = new ArrayList<>();
        for (Department department : departments) {
            Map item = new HashMap<>();
            item.put("department_id",department.getDepartmentId());
            item.put("department_name",department.getDepartmentName());
            item.put("level",department.getLevel());
            item.put("pid",department.getPid());
            item.put("opened",false);
            ArrayList newPath = (ArrayList) path.clone();
            newPath.add(department.getDepartmentId());
            item.put("path",newPath);
            item.put("children",selectDepartmentTree(company_id,department.getDepartmentId(),newPath));
            res.add(item);
        }
        return res;
    }

    @Override
    public void create(Department department) {
        this.save(department);
    }

    @Override
    public void update(Department department) {
        departmentMapper.updateById(department);
    }

    @Override
    public Department findById(String departmentId) {
        Department department = departmentMapper.selectOne(new QueryWrapper<Department>().eq("department_id", departmentId));
        return department;
    }
}
