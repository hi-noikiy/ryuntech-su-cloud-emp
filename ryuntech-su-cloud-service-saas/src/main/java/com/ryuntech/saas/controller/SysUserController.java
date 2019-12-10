package com.ryuntech.saas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.authorization.api.SysUserFeign;
import com.ryuntech.common.constant.RedisConstant;
import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.constant.enums.SmsEnum;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.common.utils.SystemTool;
import com.ryuntech.common.utils.redis.JedisUtil;
import com.ryuntech.saas.api.helper.SecurityUtils;
import com.ryuntech.saas.api.model.SysUser;
import com.ryuntech.saas.api.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

/**
 * @author antu
 * @date 2019-05-22
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(value = "SysUserController", tags = {"用户信息管理接口"})
public class SysUserController extends ModuleBaseController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    ISysUserRoleService sysUserRoleService;

    @Autowired
    ISysRoleService sysRoleService;

    @Autowired
    ISysPermService sysPermService;

    @Autowired
    MessageSendService messageSendService;

    @Autowired
    SysUserFeign sysUserFeign;

    @Autowired
    JedisUtil jedisUtil;

    /**
     * 获取当前用户信息
     *
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取当前授权用户信息", notes = "必须经过了OAuth授权")
    public Result<SysUser> info() {
        String username = getUserName();
        SysUser user = sysUserService.findByName(username);
        if (user == null) {
            return new Result<>(CommonEnums.USER_ERROR);
        }
        return new Result<>(user);
    }

    /**
     * 获取当前用户信息
     *
     * @return
     */
    @GetMapping("/cusinfo")
    @ApiOperation(value = "获取当前授权用户信息", notes = "必须经过了OAuth授权")
    public Result<SysUser> cusinfo() {
        String username = SecurityUtils.getUsername();
        SysUser user = sysUserService.findByName(username);
        if (user == null) {
            return new Result<>(CommonEnums.USER_ERROR);
        }
        return new Result<>(user);
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    @GetMapping("/info/{username}")
    @ApiOperation(value = "根据用户名查询用户信息")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    public Result<SysUser> info(@PathVariable("username") String username) {
        return new Result<>(sysUserService.findByName(username));
    }

    @PostMapping("/list")
    @ApiOperation(value = "分页、条件查询用户列表信息")
    @ApiImplicitParam(name = "user", value = "查询条件", required = true, dataType = "SysUser", paramType = "body")
    public Result<IPage<SysUser>> list(SysUser user, QueryPage queryPage) {
        //   log.info("order.getOrderid()"+user.getUsername());
        //   log.info("order.getOrderid()"+user.getId());
        return new Result<>(sysUserService.selectUsersRoleById(user, queryPage));
//        return new Result<Map>(this.selectByPageNumSize(queryPage, () -> sysUserService.list(user)));
    }


    /**
     * 根据ID查询用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询详细用户信息", notes = "id存在且大于0")
    @ApiImplicitParam(name = "id", value = "用户编号", required = true, dataType = "Long")
    public Result<SysUser> findById(@PathVariable Long id) {
        if (id == null || id == 0) {
            return new Result<>();
        } else {
            return new Result<>(sysUserService.getById(id));
        }
    }

    /**
     * 添加用户信息
     *
     * @param user
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加用户")
    @ApiImplicitParam(name = "user", value = "用户实体信息", required = true, dataType = "SysUser", paramType = "body")
    public Result add(@RequestBody SysUser user) {
        sysUserService.create(user);
        return new Result();
    }

    /**
     * 删除用户信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户")
    @ApiImplicitParam(name = "id", value = "用户编号", required = true, dataType = "Long")
    public Result delete(@PathVariable Long id) {
        sysUserService.removeById(id);
        return new Result();
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    @PutMapping("/edit")
    @ApiOperation(value = "更新用户")
    @ApiImplicitParam(name = "user", value = "用户实体信息", required = true, dataType = "SysUser", paramType = "body")
    public Result edit(@RequestBody SysUser user) {
        sysUserService.update(user);
        return new Result();
    }

    //region 登录、登出、注册

    @PostMapping("sendLoginSms")
    public Result sendLoginSms(
            @RequestParam("mobile") String mobile) {
        if (!StringUtil.isMobile(mobile)) {
            return new Result(CommonEnums.PARAM_ERROR, "手机号码不合法");
        }
        try {
            return new Result(sysUserService.sendLoginSms(mobile));
        } catch (Exception e) {
            return new Result(CommonEnums.OPERATE_ERROR, e.getLocalizedMessage());
        }
    }

    @PostMapping("/checkLoginSms")
    public Result checkLoginSms(
            @RequestParam("mobile") String mobile,
            @RequestParam("code") String code) {
        if (!StringUtil.isMobile(mobile)) {
            return new Result(CommonEnums.LOGIN_ERROR.getMsg());
        }

        return sysUserService.checkLoginSms(mobile, code);
    }

    /**
     * 账号密码登录
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Result login(String username, String password) {
        if (!StringUtil.isMobile(username)) {
            return new Result(CommonEnums.LOGIN_ERROR.getMsg());
        }

        if (StringUtil.isEmpty(password)) {
            return new Result(CommonEnums.LOGIN_ERROR.getMsg());
        }

        return sysUserService.login(username, password);
    }

    /**
     * 登出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result logout() {
        String token = SystemTool.getToken();
        // 清除当前的token信息
        jedisUtil.del(RedisConstant.PRE_CURRENT_USER + token);
        return sysUserFeign.logout(token);
    }

    /**
     * 注册时发送短信验证码
     *
     * @param mobile
     * @return
     */
    @PostMapping("sendRegisterSms")
    public Result sendRegisterSms(
            @RequestParam("mobile") String mobile) {
        if (!StringUtil.isMobile(mobile)) {
            return new Result(CommonEnums.PARAM_ERROR, "手机号码不合法");
        }
        try {
            return new Result(sysUserService.sendRegisterSms(mobile));
        } catch (Exception e) {
            return new Result(CommonEnums.OPERATE_ERROR, e.getLocalizedMessage());
        }
    }

    /**
     * 核对短信验证码是否正确
     *
     * @param mobile
     * @param code
     * @return
     */
    @PostMapping("checkRegisterSmsCode")
    public Result checkRegisterSmsCode(
            @RequestParam("mobile") String mobile,
            @RequestParam("code") String code) {
        if (!messageSendService.checkSmsCode(SmsEnum.REGISTER.getStatus(), mobile, code)) {
            return new Result(CommonEnums.PARAM_ERROR, "您输入的手机校验码不正确");
        }

        // 验证码验证成功后5分钟内注册有效
        String randomId = StringUtil.getUUID32();
        jedisUtil.setex(RedisConstant.PRE_ALLOW_REGISTER + randomId, RedisConstant.PRE_ALLOW_REGISTER_EXPIRE, mobile);
        return new Result(randomId);
    }

    /**
     * 注册保存
     *
     * @param companyName
     * @param employeeName
     * @param password
     * @param uId
     * @return
     */
    @PostMapping("register")
    public Result register(
            @RequestParam("companyName") String companyName,
            @RequestParam("employeeName") String employeeName,
            @RequestParam("password") String password,
            @RequestParam("uId") String uId) {
        companyName = StringUtil.trim(companyName);
        employeeName = StringUtil.trim(employeeName);

        if (!StringUtil.length(companyName, 1, 100)) {
            return new Result(CommonEnums.PARAM_ERROR, "公司名称仅支持1-100个字符");
        }

        if (!StringUtil.length(employeeName, 1, 50)) {
            return new Result(CommonEnums.PARAM_ERROR, "员工名称仅支持1-50个字符");
        }

        if (password == null || !Pattern.matches("^[0-9a-zA-Z~!@#$%^&*?]{6,16}$", password)) {
            return new Result(CommonEnums.PARAM_ERROR, "登录密码仅支持6-16个字符");
        }

        if (!jedisUtil.exists(RedisConstant.PRE_ALLOW_REGISTER + uId)) {
            return new Result(CommonEnums.PARAM_ERROR, "请重新获取验证码后再注册");
        }
        String mobile = jedisUtil.get(RedisConstant.PRE_ALLOW_REGISTER + uId);

        try {
            sysUserService.saveRegister(companyName, employeeName, mobile, password);
        } catch (Exception e) {
            return new Result(CommonEnums.OPERATE_ERROR, e.getLocalizedMessage());
        }

        return new Result();
    }

    //endregion
}
