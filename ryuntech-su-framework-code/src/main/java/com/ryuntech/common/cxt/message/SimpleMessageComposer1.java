package com.ryuntech.common.cxt.message;


import java.util.HashMap;
import java.util.Map;

import com.ryuntech.common.cxt.toos.ByteBuffer;
import com.ryuntech.common.cxt.toos.FileTools;

/**
 * 简单报文组织者
 * <P>
 * 将模板中的 &key; 简单替换为Map中对应 key 的 value
 * </P>
 *
 * @author liugongliang
 * @version
 */
public class SimpleMessageComposer1 implements MessageComposer {
    public static final Map reservedEntity = new HashMap();
    static{
        reservedEntity.put("amp","&");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.cxt.message.MessageComposer#compose(java.util.Map,
     *      java.lang.String)
     */
    @Override
    public byte[] compose(Map dataMap, String templateFileName)
            throws Exception {
        if (null == dataMap) {
            throw new NullPointerException("Map dataMap is null!");
        }
        if (null == templateFileName) {
            throw new NullPointerException("templateFileName is null!");
        }

        //1. 加载模板文件
        byte[] template = FileTools.loadData(templateFileName);

        //2. 填充数据
        ByteBuffer buf = new ByteBuffer(template.length);
        byte b = 0;
        String entityName = null; //实体名
        String entityValue = null;	//实体值
        int entityStartIndex = 0; //实体名开始位置
        int entityEndIndex = 0; //实体名结束位置
        int currentIndex = 0;
        while (currentIndex < template.length) {
            b = template[currentIndex];
            if (b == (byte) '&') {
                entityStartIndex = ++currentIndex;
                while (currentIndex < template.length) {
                    b = template[currentIndex];
                    if (b == (byte) ';') {
                        entityEndIndex = currentIndex++;
                        break;
                    }
                    currentIndex++;
                }
                if (b == (byte) ';') {
                    entityName = new String(template, entityStartIndex,
                            entityEndIndex - entityStartIndex);
                    if(reservedEntity.containsKey(entityName)){
                        entityValue = (String) reservedEntity.get(entityName);
                    }else{
                        entityValue = (String) MessageMapTools.getValue(dataMap, entityName);
                    }
                    if(entityValue!=null){
                        buf.append(entityValue.getBytes());
                    }

                } else {
                    throw new Exception("ReplaceableWord must start with '&' and must be terminated by the matching ';'");
                }

            }else{
                buf.append(b);
                currentIndex++;
            }

        }
        return buf.toBytes();
    }







}
