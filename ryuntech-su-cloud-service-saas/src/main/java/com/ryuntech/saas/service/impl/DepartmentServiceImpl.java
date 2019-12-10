package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryuntech.common.constant.enums.DataTypeEnum;
import com.ryuntech.common.constant.enums.ExceptionEnum;
import com.ryuntech.common.constant.generator.IncrementIdGenerator;
import com.ryuntech.common.constant.generator.UniqueIdGenerator;
import com.ryuntech.common.exception.YkServiceException;
import com.ryuntech.common.model.CurrentUser;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.common.utils.SystemTool;
import com.ryuntech.common.utils.redis.JedisUtil;
import com.ryuntech.saas.api.dto.DepartmentDataTypeTreeNodeDTO;
import com.ryuntech.saas.api.dto.DepartmetnTreeNodeDTO;
import com.ryuntech.saas.api.form.DepartmentForm;
import com.ryuntech.saas.api.mapper.DepartmentMapper;
import com.ryuntech.saas.api.mapper.EmployeeMapper;
import com.ryuntech.saas.api.model.Department;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.service.IDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
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
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private JedisUtil jedisUtil;

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
        List<Department> departments = departmentMapper.selectList(new QueryWrapper<Department>().eq("company_id", company_id).eq("pid", pid));
        List<Map> res = new ArrayList<>();
        for (Department department : departments) {
            Map item = new HashMap<>();
            item.put("department_id", department.getDepartmentId());
            item.put("department_name", department.getDepartmentName());
            item.put("level", department.getLevel());
            item.put("pid", department.getPid());
            item.put("opened", false);
            ArrayList newPath = (ArrayList) path.clone();
            newPath.add(department.getDepartmentId());
            item.put("path", newPath);
            item.put("children", selectDepartmentTree(company_id, department.getDepartmentId(), newPath));
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
        if (StringUtils.isNotBlank(department.getCompanyId())) {
            queryWrapper.eq("company_id", department.getCompanyId());
        }
        List<Department> departments = baseMapper.selectList(queryWrapper);
        return departments;
    }

    @Override
    public List<DepartmetnTreeNodeDTO> getDepartmentTree() {
        CurrentUser employee = SystemTool.currentUser(jedisUtil);
        if (employee == null) {
            throw new YkServiceException(ExceptionEnum.USER_NOT_FOUND);
        }
        String companyId = employee.getCompanyId();
        return departmentMapper.getDepartmentTreeByCompanyId(companyId);
    }

    @Override
    public void edit(DepartmentForm form) {
        CurrentUser employee = SystemTool.currentUser(jedisUtil);

        if (employee == null) {
            throw new YkServiceException(ExceptionEnum.USER_NOT_FOUND);
        }
        String empId = employee.getEmployeeId();
        String companyId = employee.getCompanyId();

        // 查询同名部门
        Department sameNameDept = baseMapper.selectOne(
                new QueryWrapper<Department>().eq("DEPARTMENT_NAME", form.getDeptName()).eq("COMPANY_ID", companyId));
        // 检测同名部门是否存在(存在同名部门, 且部门id不同)
        if (sameNameDept != null && !sameNameDept.getDepartmentId().equals(form.getDeptId())) {
            throw new YkServiceException(ExceptionEnum.DEPARTMENT_IS_FOUND);
        }
        // 父id非空, 检查父级部门是否存在, 级别是否过高
        int level = 1;
        if (!StringUtil.isEmpty(form.getParentId())) {
            Department parentDept = baseMapper.selectOne(
                    new QueryWrapper<Department>().eq("DEPARTMENT_ID", form.getParentId()).eq("COMPANY_ID", companyId));
            if (parentDept == null) {
                throw new YkServiceException(ExceptionEnum.DEPARTMENT_NOT_FOUND);
            }
            int parentLevel = Integer.parseInt(parentDept.getLevel());
            if (parentLevel >= 4) {
                throw new YkServiceException(ExceptionEnum.DEPARTMENT_ERROR_1);
            }
            level = parentLevel + 1;
        } else {
            // 父级id 为空, 设为空字符串以清除pid
            form.setParentId("");
        }

        // 数据库实体
        Department newDept = form.convertToDepartment();

        if (!StringUtil.isEmpty(newDept.getDepartmentId())) {
            // 修改部门
            String deptId = newDept.getDepartmentId();
            // 旧部门是否存在
            Department oldDept = baseMapper.selectOne(new QueryWrapper<Department>().eq("DEPARTMENT_ID", deptId).eq("COMPANY_ID", companyId));
            if (oldDept == null) {
                throw new YkServiceException(ExceptionEnum.DEPARTMENT_ERROR_2);
            }

            // 不能选择自己为父级部门
            if (newDept.getDepartmentId().equals(newDept.getPid())) {
                throw new YkServiceException(ExceptionEnum.DEPARTMENT_ERROR_3);
            }
            // 更新部门
            newDept.setLevel(String.valueOf(level));
            baseMapper.update(newDept, new QueryWrapper<Department>().eq("DEPARTMENT_ID", deptId).eq("COMPANY_ID", companyId));
            log.info("员工【{}】修改了部门：{}", empId, newDept);
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
            log.info("员工【{}】创建了部门：{}", empId, newDept);
        }
    }

    @Override
    public void delete(String deptId) {
        CurrentUser employee = SystemTool.currentUser(jedisUtil);
        if (employee == null) {
            throw new YkServiceException(ExceptionEnum.USER_NOT_FOUND);
        }
        String empId = employee.getEmployeeId();
        String companyId = employee.getCompanyId();
        // 获取旧部门并检验
        Department oldDept = baseMapper.selectOne(new QueryWrapper<Department>().eq("DEPARTMENT_ID", deptId).eq("COMPANY_ID", companyId));
        if (oldDept == null) {
            throw new YkServiceException(ExceptionEnum.DEPARTMENT_ERROR_2);
        }
        // 至少保留一个一级部门
        if ("1".equals(oldDept.getLevel())) {
            List<Department> lv1DeptList = baseMapper.selectList(new QueryWrapper<Department>().eq("LEVEL", "1").eq("COMPANY_ID", companyId));
            if (lv1DeptList.size() == 1) {
                throw new YkServiceException(ExceptionEnum.DEPARTMENT_ERROR_4);
            }
        }
        // 存在下级部门, 不能删
        List<Department> subDeptList = baseMapper.selectList(new QueryWrapper<Department>().eq("PID", deptId).eq("COMPANY_ID", companyId));
        if (subDeptList.size() > 0) {
            throw new YkServiceException(ExceptionEnum.DEPARTMENT_ERROR_5);
        }
        // 存在关联员工, 不能删
        List<Employee> employeeList = employeeMapper.selectList(new QueryWrapper<Employee>().eq("DEPARTMENT_ID", deptId).eq("COMPANY_ID", companyId));
        if (employeeList.size() > 0) {
            throw new YkServiceException(ExceptionEnum.DEPARTMENT_ERROR_6);
        }
        // 删除部门
        baseMapper.deleteById(deptId);
        log.info("员工【{}】删除了部门：{}", empId, oldDept);
    }

    @Override
    public int migrateToAnotherDept(String oldDeptId, String newDeptId) {
        CurrentUser employee = SystemTool.currentUser(jedisUtil);
        if (employee == null) {
            throw new YkServiceException(ExceptionEnum.USER_NOT_FOUND);
        }
        String empId = employee.getEmployeeId();
        String companyId = employee.getCompanyId();

        // 检查旧部门是否存在
        Department oldDept = baseMapper.selectOne(new QueryWrapper<Department>().eq("DEPARTMENT_ID", oldDeptId).eq("COMPANY_ID", companyId));
        if (oldDept == null) {
            throw new YkServiceException(ExceptionEnum.DEPARTMENT_ERROR_7);
        }
        // 检查新部门是否存在
        Department newDept = baseMapper.selectOne(new QueryWrapper<Department>().eq("DEPARTMENT_ID", newDeptId).eq("COMPANY_ID", companyId));
        if (newDept == null) {
            throw new YkServiceException(ExceptionEnum.DEPARTMENT_ERROR_8);
        }
        int result = employeeMapper.migrateToAnotherDept(oldDeptId, newDeptId);
        log.info("员工【{}】将 {} 个员工从 {} 迁移到 {}", empId, result, oldDept.getDepartmentName(), newDept.getDepartmentName());
        return result;
    }

    // 根据当前用户数据权限获取下属所有部门信息及员工id集合
    @Override
    public Result getDataTypeDepartmentTree() throws Exception {
        CurrentUser currentUser = SystemTool.currentUser(jedisUtil);
        int dataType = currentUser.getDataType();
        switch (DataTypeEnum.getByValue(dataType)) {
            case SELF:
                return new Result<>(new DepartmentDataTypeTreeNodeDTO(currentUser.getEmployeeId(), currentUser.getEmployeeName()));
            case SELF_AND_SUBORDINATE:
                // 查询该部门及其下属部门信息
                Employee employee = employeeMapper.selectOne(new QueryWrapper<Employee>().eq("employee_id", currentUser.getEmployeeId()));
                return new Result<>(new DepartmentDataTypeTreeNodeDTO(currentUser.getEmployeeId(), currentUser.getEmployeeName(), Arrays.asList(getDepartmentTreeNodeDTO(employee.getDepartmentId()))));
            case ALL:
                return new Result<>(new DepartmentDataTypeTreeNodeDTO(currentUser.getEmployeeId(), currentUser.getEmployeeName(), departmentMapper.getDepartmentTreeByCompanyId(currentUser.getCompanyId())));
            case APPOINT:
                List<DepartmetnTreeNodeDTO> rs = new ArrayList<>();
                String[] departmentIds = currentUser.getDataDepartmentId().split(",");
                for (String departmentId : departmentIds) {
                    DepartmetnTreeNodeDTO departmetnTreeNodeDTO = getDepartmentTreeNodeDTO(departmentId);
                    rs.add(departmetnTreeNodeDTO);
                }
                return new Result<>(new DepartmentDataTypeTreeNodeDTO(currentUser.getEmployeeId(), currentUser.getEmployeeName(), rs));
        }
        throw new Exception("员工：" + currentUser.getEmployeeName() + "数据权限值：" + currentUser.getDataType() + "，系统暂未匹配到该权限信息");
    }

    private DepartmetnTreeNodeDTO getDepartmentTreeNodeDTO(String departmentId) {
        CurrentUser currentUser = SystemTool.currentUser(jedisUtil);
        Department department = departmentMapper.selectOne(new QueryWrapper<Department>().eq("department_id", departmentId));
        List<DepartmetnTreeNodeDTO> rsList = new ArrayList<>();
        getChildren(rsList, currentUser.getCompanyId(), currentUser.getDataDepartmentId());
        DepartmetnTreeNodeDTO departmetnTreeNodeDTO = new DepartmetnTreeNodeDTO(department.getDepartmentName(), currentUser.getEmployeeId(), rsList);
        return departmetnTreeNodeDTO;
    }

    private void getChildren(List<DepartmetnTreeNodeDTO> rsList, String companyId, String departmentId) {
        List<DepartmetnTreeNodeDTO> list = departmentMapper.getChildren(companyId, departmentId);

        if (list != null) {
            for (DepartmetnTreeNodeDTO departmetnTreeNodeDTO : list) {
                getChildren(rsList, companyId, departmetnTreeNodeDTO.getDeptId());
            }
        }
        rsList.addAll(list);
    }

    @Override
    public List<String> getDataTypeDepartments() throws Exception {
        CurrentUser currentUser = SystemTool.currentUser(jedisUtil);
        List<String> rs = new ArrayList<>();
        int dataType = currentUser.getDataType();
        switch (DataTypeEnum.getByValue(dataType)) {
            case SELF:
                Employee employee = employeeMapper.selectOne(new QueryWrapper<Employee>().eq("employee_id", currentUser.getEmployeeId()));
                String currentDepartmentId = employee.getDepartmentId();
                rs.add(currentDepartmentId);
                return rs;
            case SELF_AND_SUBORDINATE:
                employee = employeeMapper.selectOne(new QueryWrapper<Employee>().eq("employee_id", currentUser.getEmployeeId()));
                currentDepartmentId = employee.getDepartmentId();
                rs.add(currentDepartmentId);
                getChildrenDepartmentIds(rs, currentUser.getCompanyId(), currentDepartmentId);
                return rs;
            case ALL:
                List<Department> departmentList = departmentMapper.selectList(new QueryWrapper<Department>().eq("company_id", currentUser.getCompanyId()));
                return departmentList.stream().map(d -> d.getDepartmentId()).collect(Collectors.toList());
            case APPOINT:
                String[] departmentIds = currentUser.getDataDepartmentId().split(",");
                for (String departmentId : departmentIds) {
                    rs.add(departmentId);
                    getChildrenDepartmentIds(rs, currentUser.getCompanyId(), departmentId);
                }
                return rs;
        }
        throw new Exception("员工：" + currentUser.getEmployeeName() + "数据权限值：" + currentUser.getDataType() + "，系统暂未匹配到该权限信息");
    }

    private void getChildrenDepartmentIds(List<String> rsList, String companyId, String departmentId) {
        // 查询子集
        List<DepartmetnTreeNodeDTO> list = departmentMapper.getChildren(companyId, departmentId);
        if (list != null) {
            for (DepartmetnTreeNodeDTO departmetnTreeNodeDTO : list) {
                getChildrenDepartmentIds(rsList, companyId, departmetnTreeNodeDTO.getDeptId());
            }
        }
        rsList.addAll(list.stream().map(d -> d.getDeptId()).collect(Collectors.toList()));
    }

}
