package com.ryuntech.saas.outcontroller;

import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.model.Index;
import com.ryuntech.saas.api.model.SysUser;
import com.ryuntech.saas.api.model.UserWechat;
import com.ryuntech.saas.api.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 对外接口
 * @author EDZ
 */
@Slf4j
@RestController
@RequestMapping("/outindex")
@Api(value = "IndexController", tags = {"对外首页信息接口"})
public class IndexController extends ModuleBaseController{
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    IUserWechatService userWeChatService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    IDepartmentService departmentService;

    @Autowired
    IEmployeeService employeeService;



    @Autowired
    IEmployeeDepartmentService employeeDepartmentService;
    /**
     * 首页数据展示
     *
     * @return
     */
    @PostMapping("/reportdata")
    @ApiOperation(value = "首页数据简报展示")
    public Result<Index> index(UserWechat userWechat) {
        //获取用户token，从缓存中取出用户数据
        Object openId =   redisTemplate.opsForValue().get(userWechat.getToken());
        if (openId!=null){
             //获取openid,查询用户数据
            UserWechat user1 = userWeChatService.selectByUserWeChat(userWechat);

            String userId = user1.getUserId();

            Employee employee = new Employee();
            employee.setUserId(userId);
            //获取员工
            Employee employee1 = employeeService.selectByEmployee(employee);
            String employeeId = employee1.getEmployeeId();
            //获取部门
//            employeeDepartmentService.selectByEmployeeDepartment()

            //获取部门，获取部门下所有的员工数据，获取员工相关的合同待收金额

//            SysUser sysUser = sysUserService.selectByUser(new SysUser().setId(userId));


            //统计待收


            return new Result();
        }

        return new Result();
    }
}

/*

ryn_employee(职工表)
字段名称	数据类型	非空	字段描述
EMPLOYEE_ID	VARCHAR2(64)	Y	职工编号
USER_ID	VARCHAR2(64)	Y	用户编号
NAME	VARCHAR2(255)	Y	员工姓名
COMPANY_ID 	VARCHAR2(64)	Y	所属公司编号
COMPANY_NAME	VARCHAR2(255)	N	所属公司名称


        CREATE TABLE `RYN_EMPLOYEE` (
          `EMPLOYEE_ID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '职工编号',
          `USER_ID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户编号',
          `NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '职工姓名',
          `COMPANY_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '公司编号',
          `COMPANY_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '公司姓名',
          PRIMARY KEY (`EMPLOYEE_ID`) USING BTREE
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
*/

