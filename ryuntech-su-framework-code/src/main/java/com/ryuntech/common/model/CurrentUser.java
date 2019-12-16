package com.ryuntech.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUser {
    private String sysUserId;
    private String companyId;
    private String employeeId;
    private String employeeName;
    private Integer dataType;
    private String dataDepartmentId;
    private Set<String> uris;
    private Set<String> keys;
}

//#TODO 修改员工的数据权限信息，刷新这个对象

