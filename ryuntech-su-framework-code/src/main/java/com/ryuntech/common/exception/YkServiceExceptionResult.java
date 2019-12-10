package com.ryuntech.common.exception;

import com.ryuntech.common.utils.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * @author EDZ
 */

@Data
public class YkServiceExceptionResult {


    private int status;
    private String message;
    private String timestamp;

    public YkServiceExceptionResult(YkControllerException e) {
        this.status = e.getStatus();
        this.message = e.getMessage();
        this.timestamp = DateUtil.formatDate(new Date());
    }
}
