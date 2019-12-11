package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuntech.saas.api.model.Email;
import com.ryuntech.saas.api.model.PlanOverdueRemind;

import java.util.List;

/**
 * <p>
 * 应收计划逾期跟进提醒 服务类
 * </p>
 *
 * @author antu
 * @since 2019-12-03
 */
public interface IPlanOverdueRemindService extends IService<PlanOverdueRemind> {

    /**
     * 应收计划逾期提醒邮件
     * @return
     */
    List<Email> getEmails();

}
