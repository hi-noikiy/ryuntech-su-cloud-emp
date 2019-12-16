package com.ykdtech.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ryuntech.common.constant.RabbitMqConstants.*;


/**
 * @author wanqh
 * @date 2018/01/23 15:11
 * @description: RabbitMQ的配置类，用来配队列、交换器、路由等高级信息
 */
@Configuration
public class RabbitConfig {
    @Bean
    public Queue helloConfig(){
        return new Queue("hello");
    }

    //region 直连模式
    /**
     * <p>直连模式: 通过 key 分发消息到指定队列</p>
     * <p>
     * 先声明一个直连交换机, 以及需要绑定到该直连交换机的队列.<br >
     * 再将每个队列绑定到交换机, 指定该绑定的 "路由key", 可以与队列名不同 <br>
     * 生产者通过指定 "交换机名+路由key", 指定消息要发送到哪个队列. <br>
     * 消费者通过"队列名"获取消息
     * </p>
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(EXCHANGE_DIRECT);
    }
    @Bean(QUEUE_D1)
    public Queue queueD1() {
        return new Queue(QUEUE_D1,true);
    }
    @Bean(QUEUE_D2)
    public Queue queueD2() {
        return new Queue(QUEUE_D2,true);
    }
    @Bean
    public Binding bindingDirectQueue1(@Qualifier(QUEUE_D1) Queue queueD1, DirectExchange directExchange){
        return BindingBuilder.bind(queueD1).to(directExchange).with(KEY_D1);
    }
    @Bean
    public Binding bindingDirectQueue2(@Qualifier(QUEUE_D2) Queue queueD2, DirectExchange directExchange){
        return BindingBuilder.bind(queueD2).to(directExchange).with(KEY_D2);
    }
    //endregion


    //region 订阅(扇形)模式
    /**
     * <p>订阅(扇形)模式: 忽略 key, 将详细分发到该交换机下的所有队列, 即发布/订阅模式</p>
     * <p>
     * 先声明一个扇形交换机, 以及需要绑定到该扇形交换机的队列.<br >
     * 再将每个队列绑定到交换机, 不需要指定key, 指定了也会被忽略 <br>
     * 生产者通过指定 "交换机名", 指定消息要发送到哪个交换机下的所有队列. <br>
     * 消费者通过"队列名"获取消息
     * </p>
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(EXCHANGE_FANOUT);
    }
    @Bean(QUEUE_F1)
    public Queue queueF1() {
        return new Queue(QUEUE_F1,true);
    }
    @Bean(QUEUE_F2)
    public Queue queueF2() {
        return new Queue(QUEUE_F2,true);
    }
    @Bean
    public Binding bindingFanoutQueue1(@Qualifier(QUEUE_F1) Queue queueF1, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queueF1).to(fanoutExchange);
    }
    @Bean
    public Binding bindingFanoutQueue2(@Qualifier(QUEUE_F2) Queue queueF2, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queueF2).to(fanoutExchange);
    }
    //endregion

    //region 匹配(主题)模式

    /**
     * <p>匹配(主题)模式: 与直连模式类似, 通过 key 指定需要分发消息的队列</p>
     * <p>
     * 先声明一个主题交换机, 以及需要绑定到主题交换机的队列.<br >
     * 再将每个队列绑定到交换机, 指定该绑定的 "路由key", 可以与队列名不同 <br>
     * 主题模式的key是一个匹配表达式, 以句点"."为分隔符, "*"匹配一个词, "#"匹配任意多个词<br>
     * 形如 "xxx.*", "xxx.#", "xxx.*.yyy.#", <br>
     *
     * 消息生产者通过指定 "交换机名+路由key", 指定消息要发送到哪个队列. <br>
     * 消息消费者通过"队列名"获取消息
     * </p>
     */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE_TOPIC);
    }
    @Bean(QUEUE_USA)
    public Queue usaQueue() {
        return new Queue(QUEUE_USA,true);
    }
    @Bean(QUEUE_CHINA)
    public Queue chinaQueue() {
        return new Queue(QUEUE_CHINA,true);
    }
    @Bean(QUEUE_NEWS)
    public Queue newsQueue() {
        return new Queue(QUEUE_NEWS,true);
    }
    @Bean(QUEUE_WEATHER)
    public Queue weatherQueue() {
        return new Queue(QUEUE_WEATHER,true);
    }
    @Bean
    public Binding bindingUsaQueue1(@Qualifier(QUEUE_USA) Queue queue, TopicExchange topicExchange){
        // usa.*: 仅匹配形如 usa.x 的key, 忽略 usa.x.y, usa.x.y.z 等key
        return BindingBuilder.bind(queue).to(topicExchange).with("usa.*");
    }
    @Bean
    public Binding bindingTopicQueue1(@Qualifier(QUEUE_CHINA) Queue queue, TopicExchange topicExchange){
        // china.#: 匹配形如 china.x, china.x.y, china.x.y.z 等key
        return BindingBuilder.bind(queue).to(topicExchange).with("china.#");
    }
    @Bean
    public Binding bindingTopicQueue2(@Qualifier(QUEUE_NEWS) Queue queue, TopicExchange topicExchange){
        // #.news: 仅匹配形如 x.news 的key, 忽略 x.y.news, x.y.z.news 等key
        return BindingBuilder.bind(queue).to(topicExchange).with("*.news");
    }
    @Bean
    public Binding bindingTopicQueue3(@Qualifier(QUEUE_WEATHER) Queue queue, TopicExchange topicExchange){
        // #.weather: 匹配形如 x.weather, x.y.weather, x.y.z.weather 等key
        return BindingBuilder.bind(queue).to(topicExchange).with("#.weather");
    }
    //endregion
}
