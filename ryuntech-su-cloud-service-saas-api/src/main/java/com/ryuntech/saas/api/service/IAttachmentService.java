package com.ryuntech.saas.api.service;


import com.ryuntech.saas.api.model.Attachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 附件表 服务类
 * </p>
 *
 * @author antu
 * @since 2019-10-17
 */
public interface IAttachmentService extends IBaseService<Attachment> {

    /**
     * 批量插入数据
     * @param attachments
     * @return
     */
    Boolean insertBatch(List<Attachment> attachments);


    /**
     * 根据文件编码查询
     * @param attachmentCode
     * @return
     */
    List<Attachment> selectByAttachmentCode( String attachmentCode);
}
