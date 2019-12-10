package com.ryuntech.common.exception;

import com.ryuntech.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author antu
 * @date 2019-05-22
 */
@Slf4j
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalYkExceptionHandler {

    @ExceptionHandler(YkControllerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handlerYkControllerException(YkControllerException e) {
        log.error("全局异常处理，error >> {}", e.getMessage());
        return new Result(e);
    }


    @ExceptionHandler(YkServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handlerYkServiceException(YkServiceException e) {
        log.error("全局异常处理，error >> {}", e.getMessage());
        return new Result(e);
    }
}
