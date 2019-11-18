package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.mapper.FollowupRecordCommentMapper;
import com.ryuntech.saas.api.model.FollowupRecordComment;
import com.ryuntech.saas.api.service.IFollowupRecordCommentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 跟进评论表 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-11-15
 */
@Service
public class FollowupRecordCommentServiceImpl extends BaseServiceImpl<FollowupRecordCommentMapper, FollowupRecordComment> implements IFollowupRecordCommentService {

    @Override
    public Boolean addFollowupRecordComment(FollowupRecordComment followupRecordComment) {
        int insert = baseMapper.insert(followupRecordComment);
        if (insert>0){
            return true;
        }else {
            return false;
        }
    }
}
