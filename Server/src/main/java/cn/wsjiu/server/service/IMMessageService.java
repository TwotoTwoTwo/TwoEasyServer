package cn.wsjiu.server.service;

import cn.wsjiu.server.entity.IM.Message;

public interface IMMessageService {
    /**
     * 推送消息
     * @param message 消息
     */
    void sendMessage(Message message);

    /**
     * 推送消息到用户
     * @param userId 用户id
     * @param message 消息
     * @return 发送成功返回true
     */
    boolean sendMessageToUser(String userId, Message message);

    /**
     * 广播消息给某个topic内的用户
     * @param topicId 广播id
     * @param message 消息
     * @return 发送成功返回true
     */
    boolean sendMessageToPriceTopic(String topicId, Message message);
}
