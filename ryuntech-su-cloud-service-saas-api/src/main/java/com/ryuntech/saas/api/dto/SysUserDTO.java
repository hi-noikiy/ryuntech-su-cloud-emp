package com.ryuntech.saas.api.dto;

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
public class SysUserDTO extends BaseDto {


    /**
     * 主键
     */
    @Id
    private String sysUserId;
    private String unionId;

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
    private String mobile;

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
     * 市
     */
    private String province;

    /**
     * 小程序openid
     */
    private String miniprogramOpenid;

    /**
     * 公众号openid
     */
    private String gongzhonghaoOpenid;

    /**
     * 职工列表数据ryn_employee
     */
    private List<Employee> employeeList;
}
