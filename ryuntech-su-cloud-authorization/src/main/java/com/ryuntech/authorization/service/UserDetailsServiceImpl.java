package com.ryuntech.authorization.service;

        import com.ryuntech.authorization.entity.LoginUserDetail;
        import com.ryuntech.common.constant.enums.CommonEnums;
        import com.ryuntech.common.utils.Result;
        import com.ryuntech.saas.api.model.SysUser;
        import com.ryuntech.saas.api.service.RemoteUserService;
        import lombok.extern.slf4j.Slf4j;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.security.authentication.BadCredentialsException;
        import org.springframework.security.core.userdetails.UserDetails;
        import org.springframework.security.core.userdetails.UserDetailsService;
        import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 加载用户数据
 *
 * @author antu
 * @date 2019-05-24
 */
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RemoteUserService remoteUserService;

    /**
     * 密码模式获取授权令牌，数据库查询用户信息是否存在
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Result<SysUser> result = remoteUserService.info(username.toLowerCase());
        SysUser sysUser = result.getData();
        if (sysUser == null || "0".equals(sysUser.getStatus())) {
            throw new BadCredentialsException(CommonEnums.LOGIN_ERROR.getMsg());
        }

        return new LoginUserDetail(sysUser.getSysUserId(), username, sysUser.getPassword());
    }
}
