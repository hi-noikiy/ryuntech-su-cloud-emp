package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.mapper.IBaseMapper;
import com.ryuntech.saas.api.dto.ContractRecordDTO;
import com.ryuntech.saas.api.dto.FollowupRecordDTO;
import com.ryuntech.saas.api.form.ContractRecordForm;
import com.ryuntech.saas.api.form.CustomerUserInfoForm;
import com.ryuntech.saas.api.form.FollowupRecordForm;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import com.ryuntech.saas.api.model.FollowupRecord;
import org.apache.ibatis.annotations.Param;

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
    List<ContractRecordDTO> contractRecordList(@Param("contractRecordForm") ContractRecordForm contractRecordForm);


    /**
     * 分页查询跟进信息
     * @param page
     * @param followupRecordForm
     * @return
     */
    IPage<FollowupRecordDTO> selectPageList(@Param("pg") Page<FollowupRecord> page, @Param("followupRecordForm") FollowupRecordForm followupRecordForm);

}
