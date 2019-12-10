package com.ryuntech.common.exception;

import com.ryuntech.common.constant.enums.ExceptionEnum;
import lombok.Data;

/**
 * @author EDZ
 */

@Data
public class YkServiceException extends RuntimeException{

    private Integer status;

    public YkServiceException(Integer status, String message) {
        super(message);
        this.status = status;
    }

    public YkServiceException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.status = exceptionEnum.getStatus();
    }
}
