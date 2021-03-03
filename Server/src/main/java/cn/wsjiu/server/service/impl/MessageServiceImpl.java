package cn.wsjiu.server.service.impl;

import cn.wsjiu.server.dao.MessageDAO;
import cn.wsjiu.server.entity.IM.Message;
import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.result.ResultCode;
import cn.wsjiu.server.service.MessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Resource
    MessageDAO messageDAO;

    @Override
    public Result<Integer> insertChatMessage(Message message) {
        try{
            int msgId = messageDAO.insert(message);
            if(msgId >= 0) {
                return new Result<>(msgId);
            }
            return new Result<>(ResultCode.MYSQL_ERROR);
        }catch (Exception e) {
            return new Result<>(ResultCode.SERVER_ERROR.getCode(), e.toString());
        }
    }

    @Override
    public Result<List<Message>> queryUnReadMessageByUserId(Integer userId) {
        try{
            List<Message> messageList = messageDAO.queryUnReadByUserId(userId);
            if(messageList != null && messageList.size() > 0) {
                List<Integer> msgIdList = messageList.stream().collect(
                        ArrayList::new,
                        (list, chatMessage) ->{list.add(chatMessage.getMsgId());},
                        ArrayList::addAll);
                if(msgIdList.size() > 0) {
                    updateMessageToRead(msgIdList);
                }
            }
            return new Result<>(messageList);
        }catch (Exception e) {
            return new Result<>(ResultCode.SERVER_ERROR.getCode(), e.toString());
        }
    }

    /**
     * 更新指定消息为已读
     * @param msgIdList 消息id的集合
     * @return 返回更新成功的记录数
     */
    private int updateMessageToRead(List<Integer> msgIdList) {
        return messageDAO.updateToRead(msgIdList);
    };
}
