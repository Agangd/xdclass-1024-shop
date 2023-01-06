package net.xdclass.mq;


import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.model.CouponRecordMessage;
import net.xdclass.service.CouponRecordService;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.locks.Lock;

@Slf4j
@Component
@RabbitListener(queues = "${mqconfig.coupon_release_queue}")
public class CouponMQListener {


    @Resource
    private CouponRecordService couponRecordService;

//    @Autowired
//    private RedissonClient redissonClient;

    /**
     * 重复消息-幂等性
     * 消费失败，重新入队后最大重试次数(未实现)
     *  如果消费失败，不重新入队，可以记录日志，然后插到数据库人工排查
     *
     * @param couponRecordMessage
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitHandler
    public void releaseCouponRecord(CouponRecordMessage couponRecordMessage, Message message, Channel channel) throws IOException {

        log.info("监听到消息:releaseCouponRecord消息内容:{}", couponRecordMessage);
        long msgTag = message.getMessageProperties().getDeliveryTag();

        Boolean flag = couponRecordService.releaseCouponRecord(couponRecordMessage);

        //防止同个解锁任务并发进入；如果是串行消费，则不需要加锁;加锁有利有弊，看项目逻辑而定
//        Lock lock = redissonClient.getLock("lock:coupon_record_release:" + couponRecordMessage.getTaskId());
//        lock.lock();
        try {
            if (flag) {
                //确认消息消费成功(false表示单机)
                channel.basicAck(msgTag, false);
            }
            else {
                log.error("释放优惠券失败 flag=false,{}",couponRecordMessage);
                //true表示可以重新入队
                channel.basicReject(msgTag,true);
            }
        } catch (IOException e) {
            log.error("释放优惠券异常:msg{},{}",couponRecordMessage,e);
            channel.basicReject(msgTag,true);
        }
//        finally {
//            lock.unlock();
//        }
    }
}
