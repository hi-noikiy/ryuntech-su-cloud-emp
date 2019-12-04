package com.ryuntech.common.exception;

/**
 * 系统业务逻辑异常, 必须提供错误信息, 错误代码可选
 *
 * @author luojbin
 * @version 1.0
 * @date 2019/8/13 17:52
 */
public class RyunBizException extends RuntimeException {
    private String code;

    public RyunBizException(String message) {
        super(message);
    }

    public RyunBizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
