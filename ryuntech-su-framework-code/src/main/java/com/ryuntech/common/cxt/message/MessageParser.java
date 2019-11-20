package com.ryuntech.common.cxt.message;
/*
 * Created on 2005-9-22
 *
 */

import java.util.Map;

/**
 * 报文解析器
 * @author liugongliang
 * @version
 */
public interface MessageParser {
    /**
     * 解析报文数据到Map中
     * @param message - 报文数据
     * @return Map
     * @throws Exception
     */
    public Map parser(byte[] message) throws Exception;

    /**
     * 取得报文ID，如交易码
     * @param message
     * @return 报文ID
     */
    public String getMessageID(Map message);
}
