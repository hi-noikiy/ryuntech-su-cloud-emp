package com.ryuntech.saas.outcontroller;


import com.ryuntech.common.utils.CopyUtil;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.SysUserDTO;
import com.ryuntech.saas.api.form.SysUserForm;
import com.ryuntech.saas.api.model.Employee;
import com.ryuntech.saas.api.model.SysUser;
import com.ryuntech.saas.api.model.UserWechat;
import com.ryuntech.saas.api.service.IEmployeeService;
import com.ryuntech.saas.api.service.IUserWechatService;
import com.ryuntech.saas.api.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ryuntech.common.constant.enums.CommonEnums.OPERATE_ERROR;

/**
 * @author EDZ
 */
@Slf4j
@RestController
@RequestMapping("/minilogin")
@Api(value = "MiniLoginController", tags = {"小程序登录接口"})
public class MiniLoginController extends ModuleBaseController{

    @Autowired
    RedisTemplate redisTemplate;


    @Autowired
    SysUserService sysUserService;

    @Autowired
    IEmployeeService iEmployeeService;

    @Autowired
    IUserWechatService iUserWechatService;
    /**
     * 注册第一步
     *
     * @return
     */
    @PostMapping("/outfrist")
    @ApiOperation(value = "登录第一步验证手机号")
    public Result<SysUserDTO> frist(@RequestBody SysUserForm sysUserForm) {
        Object value =   redisTemplate.opsForValue().get(sysUserForm.getPhone() + "ryun_code");
        if(StringUtils.isBlank(sysUserForm.getPhone())){
            return new Result(OPERATE_ERROR,"手机号不能为空");
        }
        if (value!=null&&value.toString().equals(sysUserForm.getVcode())){
            //判断手机号是否已经存在
            SysUser sysUser = sysUserService.selectByUser(new SysUser().setMobile(sysUserForm.getPhone()));
//          查询对应的职工
            log.info("开始查询对应的职工");
            log.info("开始查询对应的USERID"+sysUser.getSysUserId());
            List<Employee> employeeList = iEmployeeService.selectByEmployeeList(new Employee().setSysUserId(sysUser.getSysUserId()));
            log.info("结束查询对应的职工");
//            查询小程序
            UserWechat userWechat = iUserWechatService.selectByUserWeChat(new UserWechat().setUserId(sysUser.getSysUserId()));
            SysUserDTO sysUserDTO = new SysUserDTO();
            if (null!=employeeList&&employeeList.size()!=0){
                sysUserDTO.setEmployeeList(employeeList);
            }
            if (null!=userWechat){
                sysUserDTO.setUserWechat(userWechat);
            }

           // sysUserDTO.setUsername(sysUser.getUsername());
            sysUserDTO.setId(sysUser.getSysUserId());
            sysUserDTO.setAvatar(sysUser.getAvatar());
            sysUserDTO.setPhone(sysUser.getMobile());
            sysUserDTO.setStatus(sysUser.getStatus());
            if (sysUser!=null){
                //手机号
                return new Result(sysUserDTO);
            }else {
                return new Result(OPERATE_ERROR,"手机号不存在，请注册后登录");
            }
        }else {
            return new Result(OPERATE_ERROR,"参数异常");
        }

    }
}
