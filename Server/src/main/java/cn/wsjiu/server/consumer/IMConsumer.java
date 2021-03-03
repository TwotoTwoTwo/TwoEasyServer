package cn.wsjiu.server.consumer;

import cn.wsjiu.server.entity.IM.Message;
import cn.wsjiu.server.handler.IMHandler;
import cn.wsjiu.server.producer.OfflineIMProducer;
import com.alibaba.fastjson.JSONObject;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * IM通信的队列消费者
 * @author wsj
 */
@Component
public class IMConsumer {
    /**
     * 聊天消息队列生产者的组名
     */
    @Value("${rocketmq.consumer-group.IM}")
    private String consumerGroup;

    /**
     * nameServer的地址
     */
    @Value("${rocketmq.name-server}")
    private String nameServer;

    /**
     * rocketmq中IM消息的topic
     */
    @Value("${rocketmq.topic.IM}")
    private String iMTopic;

    @Value("{rocketmq.instance-name.IM}")
    private String instanceName;

    @Resource
    private IMHandler imHandler;

    @Resource
    private OfflineIMProducer offlineImProducer;

    private DefaultMQPushConsumer consumer;

    @PostConstruct
    public void init() {
        try {
            consumer = new DefaultMQPushConsumer();
            consumer.setNamesrvAddr(nameServer);
            consumer.setConsumerGroup(consumerGroup);
            consumer.setInstanceName(instanceName);
            consumer.setMessageListener(new IMMessageListener());
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.subscribe(iMTopic, "*");
            consumer.setMaxReconsumeTimes(1);
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }


    /**
     * im队列消费的回调处理
     */
    class IMMessageListener implements MessageListenerConcurrently {
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
            try{
                for (MessageExt msg : list
                ) {
                    String body = new String(msg.getBody());
                    Message message = JSONObject.parseObject(body, Message.class);
                    boolean sendResult = imHandler.sendMessage(message);
                    if(!sendResult) {
                        offlineImProducer.sendMessage(msg.getBody());
                    }
                }
            }catch (Exception e) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }
}
