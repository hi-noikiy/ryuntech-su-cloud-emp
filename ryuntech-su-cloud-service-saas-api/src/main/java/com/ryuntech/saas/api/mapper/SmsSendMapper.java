package com.ryuntech.saas.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryuntech.saas.api.model.SmsSend;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 短信发送表 Mapper 接口
 * </p>
 *
 * @author antu
 * @since 2019-12-04
 */
@Component
public interface SmsSendMapper extends BaseMapper<SmsSend> {

}
