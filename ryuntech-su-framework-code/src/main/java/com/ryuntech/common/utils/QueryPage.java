package com.ryuntech.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author antu
 * @date 2019-05-23
 */
@Data
@AllArgsConstructor
@ToString
public class QueryPage implements Serializable {

    /**
     * 当前页
     */
    private int pageCode;
    /**
     * 每页显示的记录数
     */
    private int pageSize;

    public int getPageCode() {
        return pageCode;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }
}
