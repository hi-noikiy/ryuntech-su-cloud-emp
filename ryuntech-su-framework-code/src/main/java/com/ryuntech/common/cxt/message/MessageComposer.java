package com.ryuntech.common.cxt.message;/*
 * Created on 2005-9-21
 *
 */


import java.util.Map;

/**
 * 报文组织者
 * <P>将Map内的数据填充到报文模板文件中生成报文</P>
 * @author liugongliang
 * @version
 */
public interface MessageComposer {
    /**
     * 将Map内的数据填充到报文模板文件中生成报文
     * @param messageMap - 包含报文数据的Map
     * @param templateFileName - 模板文件名
     * @return 报文
     */
    public byte[] compose(Map messageMap, String templateFileName) throws Exception;
}
