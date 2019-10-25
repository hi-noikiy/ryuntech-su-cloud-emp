package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.model.FollowupRecord;
import com.ryuntech.saas.api.model.Partner;

/**
 * <p>
 * 跟进表 服务类
 * </p>
 *
 * @author antu
 * @since 2019-10-24
 */
public interface IFollowupRecordService extends IBaseService<FollowupRecord> {

    /**
     * 分页查询跟进信息
     * @param followupRecord
     * @param queryPage
     * @return
     */
    Result<IPage<FollowupRecord>> pageList(FollowupRecord followupRecord, QueryPage queryPage);

}