package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.common.utils.QueryPage;
import com.ryuntech.common.utils.Result;
import com.ryuntech.saas.api.dto.CustomerUserInfoDTO;
import com.ryuntech.saas.api.mapper.CustomerRiskMapper;
import com.ryuntech.saas.api.mapper.CustomerUserInfoMapper;
import com.ryuntech.saas.api.mapper.ReceivableContractMapper;
import com.ryuntech.saas.api.model.CustomerRisk;
import com.ryuntech.saas.api.model.CustomerUserInfo;
import com.ryuntech.saas.api.model.ReceivableContract;
import com.ryuntech.saas.api.service.ICustomerUserInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户信息表 服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-09-29
 */
@Service
public class CustomerUserInfoServiceImpl extends BaseServiceImpl<CustomerUserInfoMapper, CustomerUserInfo> implements ICustomerUserInfoService {

    @Autowired
    private ReceivableContractMapper receivableContractMapper;

    @Autowired
    private CustomerRiskMapper customerRiskMapper;

    @Override
    public Result<IPage<CustomerUserInfo>> pageList(CustomerUserInfo customerUserInfo, QueryPage queryPage) {
        Page<CustomerUserInfo> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        if (customerUserInfo.getCustomerId()!=null) {
            queryWrapper.eq("customer_id", customerUserInfo.getCustomerId());
        }
        return super.pageList(queryWrapper,page);
    }

    @Override
    public Result<IPage<CustomerUserInfo>> selectPageList(CustomerUserInfo customerUserInfo, QueryPage queryPage) {
        Page<CustomerUserInfo> page = new Page<>(queryPage.getPageCode(), queryPage.getPageSize());
        return new Result(m.selectPageList(page,customerUserInfo));
    }

    @Override
    public List<Map<String, String>> selectCustomerMap(CustomerUserInfo customerUserInfo) {
        return baseMapper.selectCustomerMap(customerUserInfo);
    }

    @Override
    public CustomerUserInfoDTO selectCustomerUserInfoDTO(CustomerUserInfo customerUserInfo) {
        if (customerUserInfo.getCustomerId()!=null) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("customer_id", customerUserInfo.getCustomerId());
        }
        CustomerUserInfo cUserInfo = baseMapper.selectOne(queryWrapper);
        CustomerUserInfoDTO customerUserInfoDTO = new CustomerUserInfoDTO();
        customerUserInfoDTO.setCustomerId(cUserInfo.getCustomerId());
        customerUserInfoDTO.setCustomerName(cUserInfo.getCustomerName());
        customerUserInfoDTO.setStaffId(cUserInfo.getStaffId());
        customerUserInfoDTO.setStaffName(cUserInfo.getStaffName());
        customerUserInfoDTO.setContacts(cUserInfo.getContacts());
        customerUserInfoDTO.setContactsPhone(cUserInfo.getContactsPhone());
        customerUserInfoDTO.setAddress(cUserInfo.getAddress());
        customerUserInfoDTO.setStaffName(cUserInfo.getStaffName());
        List<ReceivableContract> receivableContractList = receivableContractMapper.selectList(
                new QueryWrapper<ReceivableContract>().
                        eq("customer_id", cUserInfo.getCustomerId()));
        customerUserInfoDTO.setReceivableContractList(receivableContractList);
        List<CustomerRisk> customerRiskList = customerRiskMapper.selectList(
                new QueryWrapper<CustomerRisk>().
                        eq("customer_id", cUserInfo.getCustomerId()));
        customerUserInfoDTO.setCustomerRiskList(customerRiskList);

//        总待收
        String balanceAmounts = baseMapper.selectAllBalanceAmounts(customerUserInfo);
        customerUserInfoDTO.setAllBalanceAmounts(balanceAmounts);
//        总收款
        String collectionAmount = baseMapper.selectAllCollectionAmount(customerUserInfo);
        customerUserInfoDTO.setAllCollectionAmount(collectionAmount);
//        总合同金额
        String allContractAmount = baseMapper.selectAllContractAmount(customerUserInfo);
        customerUserInfoDTO.setAllContractAmount(allContractAmount);

//        回款率  已回款/合同总额
        DecimalFormat df=new DecimalFormat("0.0000");
        if(StringUtils.isNotBlank(collectionAmount)||StringUtils.isNotBlank(allContractAmount)){
            String format = df.format((float) Float.parseFloat(collectionAmount) / Float.parseFloat(allContractAmount));
            customerUserInfoDTO.setBackRate(Float.parseFloat(format)*100+"");
        }

        return customerUserInfoDTO;
    }
}
