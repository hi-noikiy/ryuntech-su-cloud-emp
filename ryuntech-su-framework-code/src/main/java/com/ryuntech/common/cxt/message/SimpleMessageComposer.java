package com.ryuntech.common.cxt.message;
import java.util.HashMap;
import java.util.Map;

import com.ryuntech.common.cxt.toos.ByteBuffer;
import com.ryuntech.common.cxt.toos.FileTools;

/**
 * 简单报文组织者
 * <P>
 * 将模板中的变量 ${varible_name} 简单替换为messageMap中对应的key为varible_name的value <br />
 *
 * <DL COMPACT>
 * <DT>${</DT>
 * <DD>变量开始标志</DD>
 * <DT>}</DT>
 * <DD>变量结束标志</DD>
 * <DT>&</DT>
 * <DD>保留符号开始标志</DD>
 * <DT>;{</DT>
 * <DD>保留符号结束标志</DD>
 * <DT>&amp;</DT>
 * <DD>'&'</DD>
 * <DT>&dol;</DT>
 * <DD>'$'</DD>
 * </DL>
 *
 * 当模板文件中有字符组合"${"，不表示变量时，用 <I>&dol;{ </I>表示；有'&'字符时，用 <I>&amp; </I>表示。
 * </P>
 *
 * @author liugongliang
 * @version
 */
public class SimpleMessageComposer implements MessageComposer {
    /**
     * 保留符号表
     */
    public static final Map reservedSymbol = new HashMap();
    static {
        reservedSymbol.put("amp", "&");
        reservedSymbol.put("dol", "$");
    }

    /**
     * 保留符号开始标志
     */
    public static final byte SYMBOL_PREFIX = (byte) '&';

    /**
     * 保留符号结束标志
     */
    public static final byte SYMBOL_SUFFIX = (byte) ';';

    /*
     * (non-Javadoc)
     *
     * @see com.cxt.message.MessageComposer#compose(java.util.Map,
     *      java.lang.String)
     */
    @Override
    public byte[] compose(Map messageMap, String templateFileName)
            throws Exception {
        if (null == messageMap) {
            throw new NullPointerException("Map messageMap is null!");
        }
        if (null == templateFileName) {
            throw new NullPointerException("templateFileName is null!");
        }

        //1. 加载模板文件
        byte[] template = FileTools.loadData(templateFileName);

        //2. 填充数据
        ByteBuffer buf = new ByteBuffer(template.length);
        byte b = 0;

        String variableName = null; //变量名
        String variableValue = null; //变量值
        int variableStartIndex = 0; //变量名开始位置
        int variableEndIndex = 0; //变量名结束位置

        String symbolName = null; //符号名
        String symbolValue = null; //符号值
        int symbolStartIndex = 0; //符号名开始位置
        int symbolEndIndex = 0; //符号名结束位置

        int currentIndex = 0;
        while (currentIndex < template.length) {
            b = template[currentIndex];
            if (b == (byte) '$' && currentIndex < template.length - 1
                    && template[currentIndex + 1] == (byte) '{') {
                currentIndex++;
                variableStartIndex = ++currentIndex;
                while (currentIndex < template.length) {
                    b = template[currentIndex];
                    if (b == (byte) '}') {
                        variableEndIndex = currentIndex++;
                        break;
                    }
                    currentIndex++;
                }
                if (currentIndex <= template.length) {
                    variableName = new String(template, variableStartIndex,
                            variableEndIndex - variableStartIndex);
                    if (null == variableName || 0 == variableName.length()) {
                        throw new Exception("Variable name is NULL");
                    }
                    variableValue = (String) MessageMapTools.getValue(
                            messageMap, variableName);
                    if (variableValue != null) {
                        buf.append(variableValue.getBytes());
                    }

                } else {
                    throw new Exception(
                            "Variable must start with '${' and must be terminated by the matching '}'");
                }

            } else if (b == (byte) '&') {
                symbolStartIndex = ++currentIndex;
                while (currentIndex < template.length) {
                    b = template[currentIndex];
                    if (b == (byte) ';') {
                        symbolEndIndex = currentIndex++;
                        break;
                    }
                    currentIndex++;
                }
                //if (b == (byte) ';') {
                if (currentIndex <= template.length) {
                    symbolName = new String(template, symbolStartIndex,
                            symbolEndIndex - symbolStartIndex);
                    if (null == symbolName || 0 == symbolName.length()) {
                        throw new Exception("Symbol name is NULL");
                    }
                    symbolValue = (String) reservedSymbol.get(symbolName);
                    if (symbolValue != null) {
                        buf.append(symbolValue.getBytes());
                    } else {
                        throw new Exception("Symbol &" + symbolName
                                + "; cann't be identified");
                    }

                } else {
                    throw new Exception(
                            "ReservedSymbol must start with '&' and must be terminated by the matching ';'");
                }

            } else {
                buf.append(b);
                currentIndex++;
            }

        }
        return buf.toBytes();
    }
}
