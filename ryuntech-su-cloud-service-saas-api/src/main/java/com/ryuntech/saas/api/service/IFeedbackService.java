package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuntech.saas.api.dto.FinanceOrder;
import com.ryuntech.saas.api.model.Feedback;
import com.ryuntech.saas.api.model.FinanceUserInfo;

/**
 * <p>
 * 反馈信息表 服务类
 * </p>
 *
 * @author antu
 * @since 2019-11-14
 */
public interface IFeedbackService extends IBaseService<Feedback> {

    /**
     * 根据反馈对象查询
     * @param feedback
     * @return
     */
    Feedback selectByFeedback(Feedback feedback);
}
