package com.ryuntech.common.cxt.message;/*
 * Created on 2005-9-22
 *
 */


import java.util.HashMap;
import java.util.Map;

/**
 * 报文Map的增强工具，提供了按绝对路径取得子Map中值的方法
 * <P>
 * MessageMap是一个普通的Map，一般为HashMap，内部结构为树状结构，
 * 叶结点必须为String，其他节点为Map，例如：
 * <PRE>
 * Message：
 * <Comm>
 * 		<Head>
 * 			<TRCD>0001</TRCD>
 * 		</Head>
 * 		<Requst>
 * 			<AUNO>415599010009909</AUNO>
 * 			<CUSMINFO>
 * 				<Name>张三</Name>
 * 				<Sex>1</Sex>
 * 			</CUSMINFO>
 * 		</Requst>
 * </Comm>
 *
 * MessageMap:
 *     |
 *     +----"Head"=Map
 *     |            |
 *     |            +----"TRCD"="0001"
 *     |
 *     +----"Request"=Map
 *                  |
 *                  +----"AUNO"="415599010009909"
 *                  |
 *                  +----"CUSMINFO"=Map
 *                             |
 *                             +----"Name"="张三"
 *                             |
 *                             +----"Sex"="1"
 *
 * </PRE>
 * </P>
 * @author liugongliang
 * @version
 */
public class MessageMapTools {

    /**
     * 从报文Map中按带路径的键取得值
     * <P>
     * pathKey 格式为 subMapKey.*key<BR />
     * <PRE>
     * 例如：
     *     pathKey = "public.accountInfo.accountNumber"
     *     Map层次结构为：
     *     root Map
     *     ------"public" = Map
     *           ------"accountInfo" = Map
     *                 ------"accountNumber"="123456"
     *
     * </PRE>
     * </P>
     * @param messageMap
     * @param pathKey
     * @return String值或者子Map
     */
    public static Object getValue(Map messageMap, String pathKey) {
        int dotIndex = pathKey.indexOf(".");
        if(-1==dotIndex){
            return (String)messageMap.get(pathKey);
        }

        int startIndex = 0;
        String subMapKey = pathKey.substring(startIndex,dotIndex);
        Object subMap = messageMap.get(subMapKey);
        if(null==subMap || !(subMap instanceof Map)){
            return null;
        }
        return getValue((Map)subMap, pathKey.substring(dotIndex+1));
    }

    /**
     * 按路径往messageMap中设置值
     * @param messageMap - 报文Map
     * @param pathKey - 含路径的键
     * @param value - 值
     */
    public static void setValue(Map messageMap, String pathKey, Object value) {
        int dotIndex = pathKey.indexOf(".");
        if(-1==dotIndex){
            if(!messageMap.containsKey(pathKey)){
                messageMap.put(pathKey, value);
            }
            return;
        }

        int startIndex = 0;
        String subMapKey = pathKey.substring(startIndex,dotIndex);
        Object subMap = messageMap.get(subMapKey);
        if(null==subMap || !(subMap instanceof Map)){
            subMap = new HashMap();
            messageMap.put(subMapKey, subMap);
        }
        setValue((Map)subMap, pathKey.substring(dotIndex+1), value);

    }

}
