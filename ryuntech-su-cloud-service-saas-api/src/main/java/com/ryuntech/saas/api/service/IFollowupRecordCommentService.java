package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.FinanceOrder;
import com.ryuntech.saas.api.model.FollowupRecordComment;

/**
 * <p>
 * 跟进评论表 服务类
 * </p>
 *
 * @author antu
 * @since 2019-11-15
 */
public interface IFollowupRecordCommentService extends IBaseService<FollowupRecordComment> {


    /**
     * 添加跟进评论信息
     * @param followupRecordComment
     * @return
     */
    Boolean addFollowupRecordComment(FollowupRecordComment followupRecordComment);
}
