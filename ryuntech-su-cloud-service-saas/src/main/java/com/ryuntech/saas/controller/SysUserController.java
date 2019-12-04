package com.ryuntech.saas.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryuntech.common.constant.enums.CommonEnums;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.common.utils.StringUtil;
import com.ryuntech.common.utils.redis.JedisUtil;
import com.ryuntech.saas.api.helper.SecurityUtils;
import com.ryuntech.saas.api.model.SysUser;
import com.ryuntech.saas.api.service.ISysPermService;
import com.ryuntech.saas.api.service.ISysRoleService;
import com.ryuntech.saas.api.service.ISysUserRoleService;
import com.ryuntech.saas.api.service.SysUserService;
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

    @PostMapping("sendRegisterSms")
    public Result sendRegisterSms(
            @RequestParam("mobile") String mobile) {
        // TODO 手机号码已经存在sysuser表，则不允许继续注册，后面走添加公司流程


        //
        return new Result();
    }

    @PostMapping("checkRegisterSmsCode")
    public Result checkRegisterSmsCode(
            @RequestParam("mobile") String mobile) {


        // 校验成功保存随机生成uId，下一步保存校验需要使用
        return new Result();
    }

    @PostMapping("register")
    public Result register(
            @RequestParam("companyName") String companyName,
            @RequestParam("employeeName") String employeeName,
            @RequestParam("password") String password,
            // @RequestParam("uId") String uId) { 暂时短信没接
            @RequestParam("mobile") String mobile) {
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

        // TODO 通过uId获取校验成功的手机号码
        // 通过uId获取校验成功的手机号码
        //String mobile = null;

        try {
            sysUserService.saveRegister(companyName, employeeName, mobile, password);
        } catch (Exception e) {
            return new Result(CommonEnums.OPERATE_ERROR, e.getLocalizedMessage());
        }

        return new Result();
    }

}
