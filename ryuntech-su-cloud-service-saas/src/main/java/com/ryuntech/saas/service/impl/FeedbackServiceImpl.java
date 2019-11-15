package com.ryuntech.saas.service.impl;

import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.saas.api.mapper.FeedbackMapper;
import com.ryuntech.saas.api.model.Feedback;
import com.ryuntech.saas.api.service.IFeedbackService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 反馈信息表 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-11-14
 */
@Service
public class FeedbackServiceImpl extends BaseServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {

    @Override
    public Feedback selectByFeedback(Feedback feedback) {
        if (StringUtils.isNotBlank(feedback.getFeedbackId())){
            queryWrapper.eq("feedback_id",feedback.getFeedbackId());
        }
        if (StringUtils.isNotBlank(feedback.getFeedbackType())){
            queryWrapper.eq("feedback_type",feedback.getFeedbackType());
        }
        return baseMapper.selectOne(queryWrapper);
    }
}
