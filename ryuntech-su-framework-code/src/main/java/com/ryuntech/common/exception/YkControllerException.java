package com.ryuntech.common.exception;

import com.ryuntech.common.constant.enums.ExceptionEnum;
import lombok.Data;

/**
 * @author EDZ
 */

@Data
public class YkControllerException extends RuntimeException{

    private Integer status;

    public YkControllerException(Integer status, String message) {
        super(message);
        this.status = status;
    }

    public YkControllerException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.status = exceptionEnum.getStatus();
    }
}
