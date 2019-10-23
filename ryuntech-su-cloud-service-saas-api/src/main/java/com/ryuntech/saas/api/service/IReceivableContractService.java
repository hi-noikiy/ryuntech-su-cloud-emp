package com.ryuntech.saas.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.ReceivableCollectionPlanDTO;
import com.ryuntech.saas.api.dto.ReceivableContractDTO;
import com.ryuntech.saas.api.model.Attachment;
import com.ryuntech.saas.api.model.PaymentResult;
import com.ryuntech.saas.api.model.ReceivableCollectionPlan;
import com.ryuntech.saas.api.model.ReceivableContract;

import java.util.List;

/**
 * <p>
 * 应收合同表 服务类
 * </p>
 *
 * @author antu
 * @since 2019-09-27
 */
public interface IReceivableContractService extends IBaseService<ReceivableContract> {


    /**
     *
     * @param receivableContract
     * @param queryPage
     * @return
     */
    Result<IPage<ReceivableContract>> pageList(ReceivableContract receivableContract, QueryPage queryPage);

    Result<IPage<ReceivableContract>> selectPageList(ReceivableContract receivableContract, QueryPage queryPage);

    /**
     * 根据条件查询
     * @param receivableContract
     * @return
     */
    List<ReceivableContract> receivableContractList(ReceivableContract receivableContract);


    /**
     * 添加合同信息
     * @param attachments
     * @param receivableContract
     * @param receivableCollectionPlans
     * @return
     */
    Boolean addReceivableContract(List<Attachment> attachments,ReceivableContract receivableContract, List<ReceivableCollectionPlan> receivableCollectionPlans);
}
