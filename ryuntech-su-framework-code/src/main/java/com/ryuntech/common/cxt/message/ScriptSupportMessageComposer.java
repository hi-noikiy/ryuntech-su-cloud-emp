package com.ryuntech.common.cxt.message;
import java.util.HashMap;
import java.util.Map;


import bsh.Interpreter;
import com.ryuntech.common.cxt.toos.ByteBuffer;
import com.ryuntech.common.cxt.toos.FileTools;

/**
 * 支持BeanShell脚本的报文组织者
 * <P>
 * <OL>
 * <LI>将模板中的变量 ${varible_name} 替换为messageMap中对应的key为varible_name的value </LI>
 * <LI>执行模板中以"<%"开始"%>"结束的脚本，结果输出到报文</LI>
 * </OL>
 * </P>
 * 语法说明：
 * <DL COMPACT>
 * <DT>${</DT>
 * <DD>变量开始标志</DD>
 * <DT>}</DT>
 * <DD>变量结束标志</DD>
 * <DT><%</DT>
 * <DD>脚本开始标志</DD>
 * <DT>%></DT>
 * <DD>脚本结束标志</DD>
 * <DT>&</DT>
 * <DD>保留符号开始标志</DD>
 * <DT>;{</DT>
 * <DD>保留符号结束标志</DD>
 * <DT>&amp;</DT>
 * <DD>'&'</DD>
 * <DT>&dol;</DT>
 * <DD>'$'</DD>
 * <DT>&lt;</DT>
 * <DD>'<'</DD>
 * <DT>&gt;</DT>
 * <DD>'>'</DD>
 * </DL>
 * <P>
 * 模板文件中:<br />
 * "${"作为普通字符串时，用 <I>&dol;{</I>表示<br />
 * '&'作普通字符时，用 <I>&amp;</I>表示<br />
 * "<%"作为普通字符串时，用 <I>&lt;%</I>表示<br />
 * "%>"作为普通字符串时，用 <I>%&gt;</I>表示<br />
 * </P>
 *
 * @author liugongliang
 * @version
 */
public class ScriptSupportMessageComposer implements MessageComposer {
    /**
     * 保留符号表
     */
    public static final Map reservedSymbol = new HashMap();
    static {
        reservedSymbol.put("amp", "&");
        reservedSymbol.put("dol", "$");
        reservedSymbol.put("lt", "<");
        reservedSymbol.put("gt", ">");
    }

    /**
     * 保留符号开始标志
     */
    public static final byte SYMBOL_PREFIX = (byte) '&';

    /**
     * 保留符号结束标志
     */
    public static final byte SYMBOL_SUFFIX = (byte) ';';

    /**
     * BeanShell脚本执行器
     */
    private Interpreter bsh = null;

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

        String script = null; //脚本
        int scriptStartIndex = 0; //脚本开始位置
        int scriptEndIndex = 0; //脚本结束位置
        String executeResult = null;	//执行结果


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

            } else if(b == (byte) '<' && currentIndex < template.length - 1
                    && template[currentIndex + 1] == (byte) '%'){
                currentIndex++;
                scriptStartIndex = ++currentIndex;
                while (currentIndex < template.length) {
                    b = template[currentIndex];
                    if (b == (byte) '>' && template[currentIndex-1] == (byte)'%') {
                        scriptEndIndex = currentIndex - 1;
                        currentIndex++;
                        break;
                    }
                    currentIndex++;
                }
                if (currentIndex <= template.length) {
                    script = new String(template, scriptStartIndex,
                            scriptEndIndex - scriptStartIndex);
                    if (null == script || 0 == script.length()) {
                        throw new Exception("Script is NULL");
                    }

                    executeResult = executeScript(messageMap, script);

                    if (executeResult != null) {
                        buf.append(executeResult.getBytes());
                        executeResult = null;
                    }

                } else {
                    throw new Exception(
                            "Script must start with '<%' and must be terminated by the matching '%>'");
                }

            } else if (b == SYMBOL_PREFIX) {
                symbolStartIndex = ++currentIndex;
                while (currentIndex < template.length) {
                    b = template[currentIndex];
                    if (b == SYMBOL_SUFFIX) {
                        symbolEndIndex = currentIndex++;
                        break;
                    }
                    currentIndex++;
                }
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

    /**
     * 执行脚本<br />
     * @param messageMap - 报文Map，用作变量表
     * @param script - 脚本
     * @return 执行结果
     */
    private String executeScript(Map messageMap, String script) throws Exception{
        Interpreter executor = getScriptExecutor();
        executor.set("messageMap",messageMap);
        return (String)executor.eval(script);
    }

    /**
     * @return Returns the bsh.
     */
    private Interpreter getScriptExecutor() {
        if(null == bsh){
            bsh = new Interpreter();
        }
        return bsh;
    }
}
