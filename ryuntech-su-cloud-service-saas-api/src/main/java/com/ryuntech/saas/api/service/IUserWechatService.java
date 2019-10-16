package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuntech.saas.api.model.UserWechat;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author antu
 * @since 2019-10-15
 */
public interface IUserWechatService extends IService<UserWechat> {

    /**
     * 查询指定数据
     * @param userWechat
     * @return
     */
    UserWechat selectByUserWeChat(UserWechat userWechat);
}
