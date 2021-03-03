package cn.wsjiu.server.service;

import cn.wsjiu.server.entity.IM.Message;
import cn.wsjiu.server.result.Result;

import java.util.List;

public interface MessageService {
    /**
     * 插入数据库
     * @param message 消息实体
     * @return 返回消息的唯一 msgId
     */
    Result<Integer> insertChatMessage(Message message);

    /**
     * 查询用户所有未读消息
     * @param userId 用户id
     * @return 返回所有未读消息
     */
    Result<List<Message>> queryUnReadMessageByUserId(Integer userId);
}
