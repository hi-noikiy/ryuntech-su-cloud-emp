package com.ryuntech.common.exception;

/**
 * 系统异常, 必须提供错误信息, 错误代码可选
 *
 * @author luojbin
 * @version 1.0
 * @date 2019/9/3 15:16
 */
public class RyunSysException extends RuntimeException {
    private String code;

    public RyunSysException(String message) {
        super(message);
    }

    public RyunSysException(String code, String message) {
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
