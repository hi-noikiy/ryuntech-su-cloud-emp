package com.ryuntech.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryuntech.saas.api.dto.IndexDepartmentDTO;
import com.ryuntech.saas.api.mapper.EmployeeMapper;
import com.ryuntech.saas.api.model.Department;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.model.UserRoleLimit;
import com.ryuntech.saas.api.service.IDepartmentService;
import com.ryuntech.saas.api.service.IEmployeeService;
import com.ryuntech.zipkin.RyunCloudTestApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

//@RunWith(SpringRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={RyunCloudTestApplication.class,UserCompanysFindTest.class})
//@WebAppConfiguration
public class UserCompanysFindTest {

    @Autowired
    private IEmployeeService iEmployeeService;

    @Autowired
    private IDepartmentService iDepartmentService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    public void companyList() {
        List<Map<String, String>> companys = iEmployeeService.selectCompanys("749875506521833471");
        System.out.println(companys);

//        // 获取公司集合
//        Set<String> companySet = new HashSet<>();
//        for(Map<String, String> company : companys) {
//            companySet.add((String) company.get("companyName"));
//        }
//
//        // 获取公司相应角色
//        Set<String> roleSet = new HashSet<>();
//        List<Map<String, Set<String>>> listm = new ArrayList<>();
//        Map<String, Set<String>> mm = new HashMap<>();
//        for(String cs : companySet) {
//            for(Map<String, String> company : companys) {
//                if(cs.equals(company.get("companyName"))) {
//                    if(company.get("rval") != null) {
//                        roleSet.add((String) company.get("rval"));
//                    }
//                }
//            }
//            mm.put(cs, roleSet);
//        }
//        listm.add(mm);
//
//        // 获取公司相应权限
//        Set<String> roleSet1 = new HashSet<>();
//        List<Map<String, Set<String>>> listm1 = new ArrayList<>();
//        Map<String, Set<String>> mm1 = new HashMap<>();
//        for(String cs : companySet) {
//            for(Map<String, String> company : companys) {
//                if(cs.equals(company.get("companyName"))) {
//                    if(company.get("permVal") != null) {
//                        roleSet1.add((String) company.get("permVal"));
//                    }
//                }
//            }
//            mm1.put(cs, roleSet1);
//        }
//        listm1.add(mm1);
//
//        // 公司角色权限整合
//        List<Map<String, List<Set<String>>>> lmls = new ArrayList<>();
//        Map<String, List<Set<String>>> mls = new HashMap<>();
//        List<Set<String>> ls = null;
//
//        for(String cs : companySet) {
//            ls = new ArrayList<>();
//            for(Map<String, Set<String>> ms : listm) {
//                for(String key : ms.keySet()) {
//                    if (key.equals(cs)) {
//                        ls.add(ms.get(cs));
//                    }
//                }
//            }
//            for(Map<String, Set<String>> ms : listm1) {
//                for(String key : ms.keySet()) {
//                    if (key.equals(cs)) {
//                        ls.add(ms.get(cs));
//                    }
//                }
//            }
//            mls.put(cs, ls);
//        }
//        lmls.add(mls);
//        System.out.println(mls);
//    }

        // 获取公司集合
        Set<String> companySet = new HashSet<>();
        for (Map company : companys) {
            companySet.add((String) company.get("companyName"));
        }

        // 获取公司相应角色
        Set<String> roleSet = new HashSet<>();
        List<Map<String, Set<String>>> listm = new ArrayList<>();
        Map<String, Set<String>> mm = new HashMap<>();
        for (String cs : companySet) {
            for (Map<String, String> company : companys) {
                if (cs.equals(company.get("companyName"))) {
                    if (company.get("rval") != null) {
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
        for (String cs : companySet) {
            for (Map<String, String> company : companys) {
                if (cs.equals(company.get("companyName"))) {
                    if (company.get("permVal") != null) {
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


        for (String cs : companySet) {
            userRoleLimit = new UserRoleLimit();
            userRoleLimit.setCompany(cs);
            for (Map<String, Set<String>> ms : listm) {
                for (String key : ms.keySet()) {
                    if (key.equals(cs)) {
                        userRoleLimit.setUserRoles(ms.get(cs));
                    }
                }
            }
            for (Map<String, Set<String>> ms : listm1) {
                for (String key : ms.keySet()) {
                    if (key.equals(cs)) {
                        userRoleLimit.setUserLimit(ms.get(cs));
                    }
                }
            }
            listUserrl.add(userRoleLimit);
        }

        System.out.println(listUserrl);
    }

    @Test
    public void departmentHierarchical() {

        // 根据当前职工ID查询所属部门层级关系
        String employeeId = "749875236521833471";
        IndexDepartmentDTO indexDepartmentDTO = new IndexDepartmentDTO();
        Employee employee = iEmployeeService.getById(employeeId);
        String departmentId = null;
        if(employee != null) {
            departmentId = employee.getDepartmentId();
            indexDepartmentDTO.setDepartmentId(departmentId);
            indexDepartmentDTO.setDepartmentName(employee.getDepartmentName());
        }

        List<IndexDepartmentDTO> list = new ArrayList<>();
        list = getBack(departmentId);

        indexDepartmentDTO.setSonDepartment(list);

        System.out.println(indexDepartmentDTO);
    }

    @Test
    public void departments() {
        List<IndexDepartmentDTO> list = new ArrayList<>();
        list = getBack("749875236521833473");
        System.out.println(list);
    }

    public List<IndexDepartmentDTO> getBack(String departmentId) {
        List<Department> departmentList = iDepartmentService.list(new QueryWrapper<Department>().eq("pid",departmentId));
        List<IndexDepartmentDTO> indexDepartmentDTOList = new ArrayList<>();
        if(departmentList != null) {
            for(Department department : departmentList) {
                IndexDepartmentDTO indexDepartmentDTO = new IndexDepartmentDTO();
                indexDepartmentDTO.setDepartmentId(department.getDepartmentId());
                indexDepartmentDTO.setDepartmentName(department.getDepartmentName());
                indexDepartmentDTO.setSonDepartment(getBack(department.getDepartmentId()));
                indexDepartmentDTOList.add(indexDepartmentDTO);
            }
        } else {
            return null;
        }
        return indexDepartmentDTOList;
    }
}
