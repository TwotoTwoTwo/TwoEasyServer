package cn.wsjiu.server.consumer;

import cn.wsjiu.server.entity.IM.Message;
import cn.wsjiu.server.handler.IMHandler;
import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.service.MessageService;
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
 * 离线消息队列消费者,负责将离线消息装入数据库中
 * @author wsj
 */
@Component
public class OfflineIMConsumer {
    /**
     * 死信队列属于的消息队列的消费者组名
     * 死信队列topic结构为 %DLQ% + consumerGroup
     */
    @Value("${rocketmq.consumer-group.IM.offline}")
    private String consumerGroup;

    /**
     * nameServer的地址
     */
    @Value("${rocketmq.name-server}")
    private String nameServer;

    /**
     * rocketmq中IM消息的topic
     */
    @Value("${rocketmq.topic.IM.offline}")
    private String iMTopic;

    @Value("{rocketmq.instance-name.IM.offline}")
    private String instanceName;


    @Resource
    private IMHandler imHandler;

    @Resource
    private MessageService messageService;

    private DefaultMQPushConsumer consumer;

    @PostConstruct
    public void init() {
        try {
            consumer = new DefaultMQPushConsumer();
            consumer.setNamesrvAddr(nameServer);
            consumer.setConsumerGroup(consumerGroup);
            consumer.setInstanceName(instanceName);
            consumer.setMessageListener(new DeadMessageListener());
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.subscribe(iMTopic, "*");
            consumer.setMaxReconsumeTimes(8);
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 死信队列的回调消费
     */
    class DeadMessageListener implements MessageListenerConcurrently {
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
            try{
                for (MessageExt msg : list
                ) {
                    String body = new String(msg.getBody());
                    Message message = JSONObject.parseObject(body, Message.class);
                    boolean sendResult = imHandler.sendMessage(message);
                    if(!sendResult) {
                        Result<Integer> insertResult =
                                messageService.insertChatMessage(message);
                        if(insertResult.isSuccess()) {
                            message.setMsgId(insertResult.getData());
                            imHandler.sendMessage(message);
                        }else {
                            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                        }
                    }
                }
            }catch (Exception e) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }
}
