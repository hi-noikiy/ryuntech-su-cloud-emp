package com.ryuntech.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * String工具类
 *
 * @author zhangyi
 * @version 2.0
 * @time 2018年1月8日 下午2:46:30
 */
public class StringUtil {

    // TODO: 对于使用正则判断的方法, 都加上 null 判断, 以避免在调用处重复判断是否为 null

    /**
     * 校验数字 <br>
     * 长度：数字 n 位 <br>
     * 开头：1~9 <br>
     *
     * @param
     * @return
     * @author zhangyi
     * @date 2016年10月11日 下午4:52:41
     * @version 1.0.0
     */
    public static boolean isNum(String input, int n) {
        return isNum(input, n, n);
    }

    /**
     * 校验数字 <br>
     * 长度：数字 n ~ m 位 <br>
     * 开头：1~9 <br>
     *
     * @param
     * @return
     * @author zhangyi
     * @date 2016年10月11日 下午4:52:52
     * @version 1.0.0
     */
    public static boolean isNum(String input, int n, int m) {
        if (input == null || n < 1 || m < 1 || n > m) {
            return false;
        }
        String regex = "[1-9][0-9]{" + --n + "," + --m + "}";
        return Pattern.matches(regex, input);
    }

    /**
     * 校验数字 <br>
     * 长度：数字 n 位 <br>
     * 开头：0~9 <br>
     *
     * @param input
     * @param n
     * @return
     */
    public static boolean isNumber(String input, int n) {
        return isNumber(input, n, n);
    }

    /**
     * 校验数字 <br>
     * 长度：数字 n ~ m 位 <br>
     *
     * @param input
     * @param n
     * @param m
     * @return
     */
    public static boolean isNumber(String input, int n, int m) {
        if (input == null || n < 0 || m < 0 || n > m) {
            return false;
        }
        String regex = "[0-9]{" + n + "," + m + "}";
        return Pattern.matches(regex, input);
    }

    /**
     * 校验数字 <br>
     * 长度：不限
     *
     * @param
     * @return
     * @author zhangyi
     * @date 2016年10月11日 下午4:53:22
     * @version 1.0.0
     */
    public static boolean isNumber(String input) {
        String regex = "[0-9]+";
        return input != null && Pattern.matches(regex, input);
    }

    public static boolean isDecimal(String input) {
        String regex = "^\\d+(\\.\\d+)?$";
        return input != null && Pattern.matches(regex, input);
    }

    /**
     * 校验QQ号码 <br>
     * 长度：5~10数字<br>
     *
     * @param input
     * @return
     */
    public static boolean isQQ(String input) {
        String regex = "[1-9][0-9]{4,9}";
        return input != null && Pattern.matches(regex, input);
    }

    /**
     * 校验手机号码（单个）
     *
     * @param
     * @return
     * @author zhangyi
     * @date 2016年10月11日 下午4:53:58
     * @version 1.0.0
     */
    public static boolean isMobile(String input) {
        String regex = "1[0-9]{10}$";
        return input != null && Pattern.matches(regex, input);
    }

    /**
     * 校验任意字符长度 <br>
     * 长度： n 位 <br>
     *
     * @param
     * @return
     * @author zhangyi
     * @date 2016年10月11日 下午4:54:10
     * @version 1.0.0
     */
    public static boolean length(String input, int n) {
        return length(input, n, n);
    }

    /**
     * 校验任意字符长度 <br>
     * 长度： n ~ m 位 <br>
     *
     * @param
     * @return
     * @author zhangyi
     * @date 2016年10月11日 下午4:54:19
     * @version 1.0.0
     */
    public static boolean length(String input, int n, int m) {
        if (input == null || n < 0 || m < 0 || n > m) {
            return false;
        }
        String regex = ".{" + n + "," + m + "}";
        return Pattern.matches(regex, input);
    }

    /**
     * 匹配包括下划线的任何单词字符。 <br>
     * 长度： n 位 <br>
     *
     * @param
     * @return
     * @author zhangyi
     * @date 2016年10月11日 下午4:54:29
     * @version 1.0.0
     */
    public static boolean isWord(String input, int n) {
        return isWord(input, n, n);
    }

    /**
     * 匹配包括下划线的任何单词字符。 <br>
     * 长度： n ~ m 位 <br>
     *
     * @param
     * @return
     * @author zhangyi
     * @date 2016年10月11日 下午4:54:37
     * @version 1.0.0
     */
    public static boolean isWord(String input, int n, int m) {
        if (input == null || n < 0 || m < 0 || n > m) {
            return false;
        }
        String regex = "[a-zA-Z0-9_]{" + n + "," + m + "}";
        return Pattern.matches(regex, input);
    }

    /**
     * 匹配包括下划线的任何单词字符。 <br>
     * 长度： 不限 <br>
     *
     * @param
     * @return
     * @author zhangyi
     * @date 2016年10月11日 下午4:54:46
     * @version 1.0.0
     */
    public static boolean isWord(String input) {
        String regex = "[a-zA-Z0-9_]{1,}";
        return input != null && Pattern.matches(regex, input);
    }

    /**
     * 校验 values 是否包含 input 值，如果 input 为 null，否则返回 false
     *
     * @param ignoreCase 是否忽略大小写
     * @param input      进行比较的 String
     * @param values     进行比较的 String 集合
     * @param
     * @return
     * @author zhangyi
     * @date 2016年10月11日 下午4:55:25
     * @version 1.0.0
     */
    public static boolean contains(boolean ignoreCase, String input, String... values) {
        if (input == null) {
            return false;
        }
        for (String value : values) {
            // 忽略大小写
            if (ignoreCase && input.equalsIgnoreCase(value)) {
                return true;
            }
            // 区分大小写
            if (!ignoreCase && input.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是空字符串，为空时返回true
     *
     * @param s
     * @return
     * @author Robin Chang
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /**
     * 自定义的分隔字符串函数 例如: 1,2,3 =>[1,2,3] 3个元素 ,2,3=>[,2,3] 3个元素 ,2,3,=>[,2,3,] 4个元素 ,,,=>[,,,] 4个元素
     * <p>
     * 5.22算法修改，为提高速度不用正则表达式 两个间隔符,,返回""元素
     *
     * @param split 分割字符 默认,
     * @param src   输入字符串
     * @return 分隔后的list
     * @author Robin
     */
    public static List<String> splitToList(String split, String src) {
        // 默认,
        String sp = ",";
        if (split != null && split.length() == 1) {
            sp = split;
        }
        List<String> r = new ArrayList<>();
        int lastIndex = -1;
        int index = src.indexOf(sp);
        if (-1 == index && src != null) {
            r.add(src);
            return r;
        }
        while (index >= 0) {
            if (index > lastIndex) {
                r.add(src.substring(lastIndex + 1, index));
            } else {
                r.add("");
            }

            lastIndex = index;
            index = src.indexOf(sp, index + 1);
            if (index == -1) {
                r.add(src.substring(lastIndex + 1));
            }
        }
        return r;
    }

    /**
     * String数组转换long数组
     *
     * @param
     * @return
     * @author zhangyi
     * @date 2016年10月11日 下午4:48:19
     * @version 1.0.0
     */
    public static Long[] stringToLong(String[] stringArray) {
        if (stringArray == null || stringArray.length < 1) {
            return null;
        }
        Long[] longArray = new Long[stringArray.length];
        for (int i = 0; i < longArray.length; i++) {
            try {
                longArray[i] = Long.valueOf(stringArray[i]);
            } catch (NumberFormatException e) {
                longArray[i] = 0L;
            }
        }
        return longArray;
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     *
     * @param
     * @return 字符串的长度
     * @author yangwenbin
     * @date 2016年7月6日 上午10:30:50
     * @version 1.0.0
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 校验是否身份证号, 18位或15位
     *
     * @param input
     * @return
     */
    public static boolean isIdCardNo(String input) {
        String regex = "" +
                "(^[1-9]\\d{5}" +
                "(19|20)\\d{2}" +
                "((0[1-9])|(10|11|12))" +
                "(([0-2][1-9])|10|20|30|31)" +
                "\\d{3}" +
                "[0-9Xx]$)" +
                "|" +
                "(^[1-9]\\d{5}" +
                "\\d{2}" +
                "((0[1-9])|(10|11|12))" +
                "(([0-2][1-9])|10|20|30|31)" +
                "\\d{3}$)";
        return input != null && Pattern.matches(regex, input);
    }

    public static boolean isMail(String input) {
        String regex = "^[a-zA-Z0-9_\\-.]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        return input != null && Pattern.matches(regex, input);
    }

    public static String sixRandomNum() {
        return String.valueOf(new Random().nextDouble()).substring(3, 9);
    }

    public static String trim(String input) {
        return input == null ? null : input.trim();
    }

    // 暂时拜访记录下一步使用
    public static String undefinedTrim(String input) {
        if(isEmpty(input) || "undefined".equals(input)) {
            return null;
        }

        return input.trim();
    }
}