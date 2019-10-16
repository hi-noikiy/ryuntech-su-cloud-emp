package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.saas.api.mapper.UserWechatMapper;
import com.ryuntech.saas.api.model.UserWechat;
import com.ryuntech.saas.api.service.IUserWechatService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-10-15
 */
@Service
public class UserWechatServiceImpl extends BaseServiceImpl<UserWechatMapper, UserWechat> implements IUserWechatService {

    @Override
    public UserWechat selectByUserWeChat(UserWechat userWechat) {
        if (StringUtils.isNotBlank(userWechat.getOpenId())){
            queryWrapper.eq("OPEN_ID",userWechat.getOpenId());
        }
        return baseMapper.selectOne(queryWrapper);
    }
}
