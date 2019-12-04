package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryuntech.common.constant.generator.IncrementIdGenerator;
import com.ryuntech.common.constant.generator.UniqueIdGenerator;
import com.ryuntech.common.exception.RyunBizException;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.saas.api.dto.DepartmetnTreeNodeDTO;
import com.ryuntech.saas.api.form.DepartmentForm;
import com.ryuntech.saas.api.mapper.DepartmentMapper;
import com.ryuntech.saas.api.model.Department;
import com.ryuntech.saas.api.service.IDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
@Slf4j
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

    @Override
    public Department selectOneByWhere(Wrapper queryWrapper) {
        return departmentMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean remove(Wrapper wrapper) {
       return super.remove(wrapper);
    }

    @Override
    public List<Department> findByDepartment(Department department) {
        if (StringUtils.isNotBlank(department.getCompanyId())){
            queryWrapper.eq("company_id",department.getCompanyId());
        }
        List<Department> departments = baseMapper.selectList(queryWrapper);
        return departments;
    }

    @Override
    public List<DepartmetnTreeNodeDTO> getDepartmentTree() {
        // todo 获取公司id
        String companyId = "773031356912366360";
        return departmentMapper.getDepartmentTreeByCompanyId(companyId);
    }

    @Override
    public void edit(DepartmentForm form) {
        // todo 获取公司id
        String empName = "操作员工";
        String companyId = "773031356912366360";

        // 查询同名部门
        Department sameNameDept = baseMapper.selectOne(
                new QueryWrapper<Department>().eq("DEPARTMENT_NAME", form.getDeptName()).eq("COMPANY_ID", companyId));
        // 检测同名部门是否存在(存在同名部门, 且部门id不同)
        if (sameNameDept != null && !sameNameDept.getDepartmentId().equals(form.getDeptId())) {
            throw new RyunBizException("已经存在同名的部门, 请指定新的部门名.");
        }
        // 父id非空, 检查父级部门是否存在, 级别是否过高
        int level = 1;
        if (!StringUtil.isEmpty(form.getParentId())) {
            Department parentDept = baseMapper.selectOne(
                    new QueryWrapper<Department>().eq("DEPARTMENT_ID", form.getParentId()).eq("COMPANY_ID", companyId));
            if (parentDept ==null){
                throw new RyunBizException("上级部门不存在, 请指定新的上级部门.");
            }
            int parentLevel = Integer.parseInt(parentDept.getLevel());
            if (parentLevel >= 4) {
                throw new RyunBizException("目前系统最高仅支持 4 级部门, 请重新指定其他的上级部门.");
            }
            level = parentLevel + 1;
        }

        // 数据库实体
        Department newDept = form.convertToDepartment();

        if (!StringUtil.isEmpty(newDept.getDepartmentId())) {
            // 修改部门
            String deptId = newDept.getDepartmentId();
            // 旧部门是否存在
            Department oldDept = baseMapper.selectOne(new QueryWrapper<Department>().eq("DEPARTMENT_ID", deptId).eq("COMPANY_ID", companyId));
            if (oldDept == null) {
                throw new RyunBizException("部门不存在, 操作失败");
            }

            // 更新部门
            newDept.setLevel(String.valueOf(level));
            baseMapper.update(newDept, new QueryWrapper<Department>().eq("DEPARTMENT_ID", deptId).eq("COMPANY_ID", companyId));
            log.info("员工【{}】修改了部门：{}", empName, newDept);
        } else {
            // 新增部门, 新建一个 deptId
            UniqueIdGenerator uniqueIdGenerator = UniqueIdGenerator.getInstance(IncrementIdGenerator.getServiceId());
            String deptId = uniqueIdGenerator.nextStrId();
            // 完善部门对象的数据并插入
            newDept.setDepartmentId(deptId);
            newDept.setCompanyId(companyId);
            newDept.setLevel(String.valueOf(level));
            newDept.setCreatedAt(newDept.getUpdatedAt());
            baseMapper.insert(newDept);
            log.info("员工【{}】创建了部门：{}", empName, newDept);
        }
    }
}
