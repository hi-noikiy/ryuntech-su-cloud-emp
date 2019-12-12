package com.ryuntech.saas.api.form;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class EmployeeForm {
    private String companyId;

    private String departmentId;

    private String status;

    private String keyWord;

    private Integer pageCode;

    private Integer pageSize;

    private String employeeId;

    private String email;

    public EmployeeForm(String companyId, String departmentId, String status, String keyWord, int pageCode, int pageSize) {
        this.companyId=companyId;
        this.departmentId=departmentId;
        this.status=status;
        this.pageCode=pageCode;
        this.pageSize=pageSize;
    }
}
