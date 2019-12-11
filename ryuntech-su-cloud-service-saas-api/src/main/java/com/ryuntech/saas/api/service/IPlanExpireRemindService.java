package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuntech.saas.api.model.Email;
import com.ryuntech.saas.api.model.PlanExpireRemind;

import java.util.List;

/**
 * <p>
 * 应收计划到期提醒 服务类
 * </p>
 *
 * @author antu
 * @since 2019-12-03
 */
public interface IPlanExpireRemindService extends IService<PlanExpireRemind> {

    /**
     * 应收计划到期提醒邮件
     * @return
     */
    List<Email> getEmails();

}
