package com.ryuntech.saas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryuntech.common.service.impl.BaseServiceImpl;
import com.ryuntech.saas.api.mapper.SysParamsMapper;
import com.ryuntech.saas.api.model.SysParams;
import com.ryuntech.saas.api.service.ISysParamsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author antu
 * @since 2019-08-15
 */
@Service
public class SysParamsServiceImpl extends BaseServiceImpl<SysParamsMapper, SysParams> implements ISysParamsService {



    @Override
    public String getValueByInnerName(String innerName) {
        SysParams sysParams = baseMapper.selectOne(new QueryWrapper<SysParams>().eq("inner_name", innerName));
//        判断状态是否可用
        if (sysParams.getStatus().equals("1")){
            return sysParams.getValue();
        }
        return "";
    }

    @Override
    public String getValueByOuterName(String outerName) {
        SysParams sysParams = baseMapper.selectOne(new QueryWrapper<SysParams>().eq("outer_name", outerName));
        if (sysParams.getStatus().equals("1")){
            return sysParams.getValue();
        }
        return "";
    }
}
