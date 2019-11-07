package com.ryuntech.saas.api.dto;


import com.ryuntech.common.model.BaseModel;
import com.ryuntech.saas.api.model.ReceivableContract;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author EDZ
 */
@Data
@Accessors(chain = true)
public class WeChatIndexDetailDTO extends BaseModel {



    /**
     * 总待收金额
     */
    private String balanceAmounts;
    /**
     * 总合同数
     */
    private String allContract;
    /**
     * 总客户数
     */
    private String allCustomer;

    /**
     * 按合同显示
     */
    private List<ReceivableContract> onListContract;

    /**
     * 按客户显示
     */
    private List<ReceivableContract> onListCustomer;
    /**
     * 按员工显示
     */
    private List<ReceivableContract> onListEmployee;

}
