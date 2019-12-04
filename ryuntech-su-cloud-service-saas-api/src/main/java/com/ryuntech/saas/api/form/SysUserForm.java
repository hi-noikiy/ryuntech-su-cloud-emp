package com.ryuntech.saas.api.form;

import com.ryuntech.common.model.BaseDto;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.model.UserWechat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import java.util.List;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class SysUserForm extends BaseDto {


    /**
     * 主键
     */
    @Id
    private String id;
    private String union_id;

    /**
     * 小程序用户对象
     */
    private UserWechat userWechat;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 公司名
     */
    private String companyName;


    /**
     * 头像
     */
    private String avatar;

    /**
     * 姓名
     */
    private String name;



    /**
     * 手机验证码
     */
    private String vcode;
    /**
     * 用户状态
     */
    private String status;

    /**
     * 职工列表数据ryn_employee
     */
    private List<Employee> employeeList;
}
