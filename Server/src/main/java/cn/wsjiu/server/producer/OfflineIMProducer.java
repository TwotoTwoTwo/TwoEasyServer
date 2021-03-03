package cn.wsjiu.server.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OfflineIMProducer {
    /**
     * 聊天消息队列生产者的组名
     */
    @Value("${rocketmq.producer-group.IM.offline}")
    private String producerGroup;

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

    private String KEYS = "offline-message";


    private DefaultMQProducer defaultMQProducer;

    @PostConstruct
    public void init() {
        defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setProducerGroup(producerGroup);
        defaultMQProducer.setNamesrvAddr(nameServer);
        try {
            defaultMQProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(byte[] payLoad) {
        try {
            Message message = new Message();
            message.setBody(payLoad);
            message.setTopic(iMTopic);
            message.setKeys(KEYS);
            defaultMQProducer.send(message);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
