package com.ykdtech.rabbitmq.controller;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;

import static com.ryuntech.common.constant.RabbitMqConstants.*;

@Controller
@RequestMapping("rb")
public class DemoController {
    @Autowired
    private AmqpTemplate amqpTemplate;

    //region 直连模式
    /**
     * 直连模式 发送到直连交换机及指定队列 <br>
     *
     * 生产者通过指定 "交换机名", 指定消息要发送到哪个交换机下的所有队列. <br>
     * 消费者通过"队列名"获取消息
     */
    @GetMapping("/sendDx")
    @ResponseBody
    public String sendDx() {
        for (int i = 0; i < 10; i++) {
            amqpTemplate.convertAndSend(EXCHANGE_DIRECT, KEY_D1, "直连1 " + new Date().toString());
            amqpTemplate.convertAndSend(EXCHANGE_DIRECT, KEY_D2, "直连2 " + new Date().toString());
        }
        return "直连模式, 三个队列各发10条消息";
    }
    @RabbitHandler
    @RabbitListener(queues = QUEUE_D1)
    public void dqConsumer1(Message message, Channel channel) throws Exception {
        try {
            // 尝试消费消息, 执行业务操作
            System.out.println(">>>>>>>>111111111" + Thread.currentThread() + "dqConsumer1 收到消息:" +message);

            // 确认消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            // 消费消息出现异常, 取消消费
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
    @RabbitHandler
    @RabbitListener(queues = QUEUE_D2)
    public void dqConsumer2(Message message, Channel channel) throws Exception {
        try {
            System.out.println(">>>>>>>>22222222" + Thread.currentThread() + "dqConsumer2 收到消息:" +message);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
    //endregion

    //region 订阅模式消
    /**
     * 订阅模式 发送到扇形交换机, 不指定路由键, 交换机下所有队列都能收到消息 <br>
     *
     * 生产者通过指定 "交换机名", 指定消息要发送到哪个交换机下的所有队列. <br>
     * 消费者通过"队列名"获取消息
     */
    @GetMapping("/sendFx")
    @ResponseBody
    public String sendFx() {
        for (int i = 0; i < 3; i++) {
            amqpTemplate.convertAndSend(EXCHANGE_FANOUT, null, "订阅1 " + new Date().toString());
            amqpTemplate.convertAndSend(EXCHANGE_FANOUT, null, "订阅2 " + new Date().toString());
            amqpTemplate.convertAndSend(EXCHANGE_FANOUT, null, "订阅3 " + new Date().toString());
        }
        return "发送成功";
    }
    @RabbitHandler
    @RabbitListener(queues = QUEUE_F1)
    public void fqConsumer1(Message message, Channel channel) throws Exception {
        try {
            System.out.println(">>>>>>>>111111111" + Thread.currentThread() + "fqConsumer1 收到消息:" +message);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
    @RabbitHandler
    @RabbitListener(queues = QUEUE_F2)
    public void fqConsumer2(Message message, Channel channel) throws Exception {
        try {
            System.out.println(">>>>>>>>22222222222" + Thread.currentThread() + "fqConsumer2 收到消息:" +message);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
    //endregion


    //region 匹配(主题)模式
    /**
     * 匹配(主题)模式 发送到扇形交换机, 根据路由键的匹配情况分发消息
     *
     * 消息生产者通过指定 "交换机名+路由key", 指定消息要发送到哪个队列. <br>
     * 消息消费者通过"队列名"获取消息
     */
    @GetMapping("/sendTp")
    @ResponseBody
    public String sendTp() {
        // QUEUE_NEWS QUEUE_USA
        amqpTemplate.convertAndSend(EXCHANGE_TOPIC, "usa.news", "美国新闻联播");
        // QUEUE_NEWS QUEUE_CHINA
        amqpTemplate.convertAndSend(EXCHANGE_TOPIC, "china.news", "中国新闻联播");
        // 没有队列能收到
        amqpTemplate.convertAndSend(EXCHANGE_TOPIC, "usa.NewYork.news", "美国纽约新闻联播");
        // QUEUE_CHINA
        amqpTemplate.convertAndSend(EXCHANGE_TOPIC, "china.Shenzhen.news", "中国深圳新闻联播");

        // QUEUE_WEATHER QUEUE_USA
        amqpTemplate.convertAndSend(EXCHANGE_TOPIC, "usa.weather", "美国天气预报");
        // QUEUE_WEATHER QUEUE_CHINA
        amqpTemplate.convertAndSend(EXCHANGE_TOPIC, "china.weather", "中国天气预报");
        // QUEUE_WEATHER
        amqpTemplate.convertAndSend(EXCHANGE_TOPIC, "usa.NewYork.weather", "美国纽约天气预报");
        // QUEUE_WEATHER QUEUE_CHINA
        amqpTemplate.convertAndSend(EXCHANGE_TOPIC, "china.Shenzhen.weather", "中国深圳天气预报");
        return "发送成功";
    }
    @RabbitHandler
    @RabbitListener(queues = QUEUE_USA)
    public void usaQueue(Message message, Channel channel) throws Exception {
        try {
            System.out.println(">>>>>>>>>>>>>>" + Thread.currentThread() + "USA 收到消息:" +message);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
    @RabbitHandler
    @RabbitListener(queues = QUEUE_CHINA)
    public void chinaQueue(Message message, Channel channel) throws Exception {
        try {
            System.out.println(">>>>>>>>>>>>>>" + Thread.currentThread() + "CHINA 收到消息:" +message);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
    @RabbitHandler
    @RabbitListener(queues = QUEUE_NEWS)
    public void newsQueue(Message message, Channel channel) throws Exception {
        try {
            System.out.println(">>>>>>>>>>>>>>" + Thread.currentThread() + "NEWS 收到消息:" +message);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
    @RabbitHandler
    @RabbitListener(queues = QUEUE_WEATHER)
    public void weatherQueue(Message message, Channel channel) throws Exception {
        try {
            System.out.println(">>>>>>>>>>>>>>" + Thread.currentThread() + "WEATHER 收到消息:" +message);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        } catch (IOException e) {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
    //endregion

}
