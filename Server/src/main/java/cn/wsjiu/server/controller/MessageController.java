package cn.wsjiu.server.controller;

import cn.wsjiu.server.entity.IM.Message;
import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.result.ResultCode;
import cn.wsjiu.server.service.MessageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {

    @Resource
    MessageService messageService;

    /**
     * 查询未读消息的接口
     * @param userId 用户id
     * @return 返回所有未读消息
     */
    @RequestMapping("get")
    public Result<List<Message>> getAllUnReadMessage(int userId) {
        if(userId < 0) {
            return new Result<>(ResultCode.PARAM_ERROR);
        }
        return messageService.queryUnReadMessageByUserId(userId);
    }
}
