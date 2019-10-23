package com.ryuntech.saas.api.mapper;


import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.model.Attachment;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 附件表 Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-10-17
 */
public interface AttachmentMapper extends IBaseMapper<Attachment> {

    /**
     * 批量插入数据
     * @param attachments
     * @return
     */
    int insertBatch(List<Attachment> attachments);
}
