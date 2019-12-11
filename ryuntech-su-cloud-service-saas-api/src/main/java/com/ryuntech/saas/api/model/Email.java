package com.ryuntech.saas.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 邮件类
 * </p>
 *
 * @author wanh
 * @since 2019-12-06
 */
@Data
@Accessors(chain = true)
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;

    // 邮箱
    private String email;

    // 主题
    private String subject;

    // 模板
    private String template;

    // 显示数据
    private Map<String, Object> params;

}
