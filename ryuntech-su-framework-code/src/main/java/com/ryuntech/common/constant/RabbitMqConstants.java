package com.ryuntech.common.constant;

/**
 * RabbitMQ 相关常量
 *
 * @author luojbin
 * @version 1.0
 * @date 2019/12/16 10:03
 */
public class RabbitMqConstants {

    //region todo 示例用常量, 可以同示例代码一同删除
    //region 直连模式
    /** 直连交换机， 通过 key 分发消息到指定队列 */
    public static final String EXCHANGE_DIRECT = "directExchange";
    /** 直连模式队列1 */
    public static final String QUEUE_D1 = "queueD1";
    /** 直连模式队列2 */
    public static final String QUEUE_D2 = "queueD2";
    /** 直连队列1的key */
    public static final String KEY_D1 = "keyD1";
    /** 直连队列2的key */
    public static final String KEY_D2 = "keyD2";
    //endregion

    //region 订阅(扇形)模式
    /** 扇形交换机， 忽略 key, 将详细分发到该交换机下的所有队列 */
    public static final String EXCHANGE_FANOUT = "fanoutExchange";
    /** 订阅（扇形）模式队列1 */
    public static final String QUEUE_F1 = "queueF1";
    /** 订阅（扇形）模式队列2 */
    public static final String QUEUE_F2 = "queueF2";
    //endregion

    //region 匹配(主题)模式
    /** 主题交换机， 根据 key 匹配相应的队列 */
    public static final String EXCHANGE_TOPIC = "topicExchange";
    /** 匹配（主题）模式队列1, 接受 A.* 的消息 */
    public static final String QUEUE_USA = "usaQueue";
    /** 匹配（主题）模式队列2, 接受 B.# 的消息 */
    public static final String QUEUE_CHINA = "chinaQueue";
    /** 匹配（主题）模式队列3, 接受 *.C 的消息 */
    public static final String QUEUE_NEWS = "newsQueue";
    /** 匹配（主题）模式队列4, 接受 #.D 的消息 */
    public static final String QUEUE_WEATHER = "weatherQueue";
    //endregion
    //endregion


}