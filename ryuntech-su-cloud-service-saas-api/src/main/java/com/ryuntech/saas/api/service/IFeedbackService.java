package com.ryuntech.saas.api.service;

import com.ryuntech.saas.api.model.FeedBack;

/**
 * <p>
 * 反馈信息表 服务类
 * </p>
 *
 * @author antu
 * @since 2019-11-14
 */
public interface IFeedbackService extends IBaseService<FeedBack> {

    /**
     * 根据反馈对象查询
     * @param feedback
     * @return
     */
    FeedBack selectByFeedback(FeedBack feedback);
}
