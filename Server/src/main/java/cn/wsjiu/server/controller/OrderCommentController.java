package cn.wsjiu.server.controller;

import cn.wsjiu.server.entity.OrderComment;
import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.result.ResultCode;
import cn.wsjiu.server.service.OrderCommentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("orderComment")
public class OrderCommentController {
    @Resource
    OrderCommentService orderCommentService;

    /**
     * 订单交易完成后的评论接口
     * @param orderComment 订单评论
     * @return 返回评论是否成功
     */
    @RequestMapping("publish")
    public Result<Void> comment(@RequestBody OrderComment orderComment) {
        Result<Void> result = new Result<>(ResultCode.PARAM_ERROR);
        if(orderComment.getOrderId() == null || orderComment.getOrderId() < 0) {
            return result;
        }
        if(orderComment.getBuyerId() == null || orderComment.getBuyerId() < 0) {
            return result;
        }
        if(orderComment.getSellerId() == null || orderComment.getSellerId() < 0) {
            return result;
        }
        if(orderComment.getContent() == null || orderComment.getContent().length() <= 0) {
            return result;
        }
        result = orderCommentService.insertOrderComment(orderComment);
        return result;
    }

    /**
     * 查询用户相关的评论
     * @param userId 用户id
     * @param page 页号
     * @param pageSize 页大小
     * @return 分页返回用户的评论
     */
    @RequestMapping("get")
    public Result<List<OrderComment>> get(Integer userId, Integer page, Integer pageSize) {
        Result<List<OrderComment>> result = new Result<>(ResultCode.PARAM_ERROR);
        if(userId == null || userId < 0) {
            return result;
        }
        if(page == null || page < 0) {
            return result;
        }
        if(pageSize == null || pageSize < 0) {
            return result;
        }
        result = orderCommentService.getOrderCommentByUserId(userId, page, pageSize);
        return result;
    }
}
