package net.xdclass.config;


import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Data
public class RabbitMQConfig {

    /**
     * 交换机
     */
    @Value("${mqconfig.coupon_event_exchange}")
    private String eventExchange;
    /**
     * 第⼀个队列延迟队列，
     */

    @Value("${mqconfig.coupon_release_delay_queue}")
    private String couponReleaseDelayQueue;
    /**
     * 第⼀个队列的路由key
     * 进⼊队列的路由key
     */

    @Value("${mqconfig.coupon_release_delay_routing _key}")
    private String couponReleaseDelayRoutingKey;

    /**
     * 第⼆个队列，被监听恢复库存的队列
     */
    @Value("${mqconfig.coupon_release_queue}")
    private String couponReleaseQueue;
    /**
     * 第⼆个队列的路由key
     * <p>
     * 即进⼊死信队列的路由key
     */

    @Value("${mqconfig.coupon_release_routing_key}")
    private String couponReleaseRoutingKey;
    /**
     * 过期时间
     */
    @Value("${mqconfig.ttl}")
    private Integer ttl;


    /**
     * 消息转换器
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 创建交换机,Topic类型，也可以使用direct路由
     * 一般是一个微服务一个交换机
     * @return
     */
    @Bean
    public Exchange couponEVEntExchange(){

        return new TopicExchange(eventExchange,true,false);
    }

    /**
     * 延迟队列
     * @return
     */
    @Bean
     public Queue couponReleaseDelayQueue(){

        Map<String,Object> args = new HashMap<>();
        args.put("x-message-ttl",ttl);
        args.put("x-dead-latter-routing-key",couponReleaseDelayRoutingKey);
        args.put("x-dead-latter-exchange",eventExchange);
        return new Queue(couponReleaseDelayQueue,true,false,false,args);
     }


    /**
     * 死信队列，普通队列，用于监听
     */
    @Bean
    public Queue couponReleaseQueue(){
        return new Queue(couponReleaseQueue,true,false,false);
    }

    /**
     * 第一个队列，即延迟队列的绑定关系建立
     */
    @Bean
    public Binding couponReleaseDelayBinding(){
        return new Binding(couponReleaseDelayQueue,Binding.DestinationType.QUEUE,eventExchange,couponReleaseRoutingKey,null);
    }


    /**
     * 死信队列绑定关系建立
     */
    @Bean
    public Binding couponReleaseBinding(){
        return new Binding(couponReleaseQueue,Binding.DestinationType.QUEUE,eventExchange,couponReleaseRoutingKey,null);
    }


}
