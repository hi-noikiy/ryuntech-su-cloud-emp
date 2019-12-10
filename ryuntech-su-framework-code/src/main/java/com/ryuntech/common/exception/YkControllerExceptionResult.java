package com.ryuntech.common.exception;


import com.ryuntech.common.utils.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * @author EDZ
 */
@Data
public class YkControllerExceptionResult {

    private int status;
    private String message;
    private String timestamp;

    public YkControllerExceptionResult(YkControllerException e) {
        this.status = e.getStatus();
        this.message = e.getMessage();
        this.timestamp = DateUtil.formatDate(new Date());
    }
}
