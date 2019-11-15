package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.dto.ContractRecordDTO;
import com.ryuntech.saas.api.form.ContractRecordForm;
import com.ryuntech.saas.api.model.FollowupRecord;

import java.util.List;

/**
 * <p>
 * 跟进表 Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-10-24
 */
public interface FollowupRecordMapper extends IBaseMapper<FollowupRecord> {


    /**
     * 查询合同
     * @param contractRecordForm
     * @return
     */
    List<ContractRecordDTO> contractRecordList(ContractRecordForm contractRecordForm);
}
