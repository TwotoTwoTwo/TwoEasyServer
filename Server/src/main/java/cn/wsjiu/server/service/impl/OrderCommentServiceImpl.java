package cn.wsjiu.server.service.impl;

import cn.wsjiu.server.dao.OrderCommentDAO;
import cn.wsjiu.server.entity.OrderComment;
import cn.wsjiu.server.entity.OrderState;
import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.result.ResultCode;
import cn.wsjiu.server.service.OrderCommentService;
import cn.wsjiu.server.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderCommentServiceImpl implements OrderCommentService {
    @Resource
    OrderCommentDAO orderCommentDAO;

    @Resource
    OrderService orderService;

    @Override
    public Result<Void> insertOrderComment(OrderComment orderComment) {
        Result<Void> updateOrderState = orderService.updateOrderState(orderComment.getOrderId(),
                OrderState.TRANSACTION_FINISH.mask, OrderState.TRANSACTION_COMMENTED.mask);
        if(updateOrderState.isSuccess()) {
            int insertResult = orderCommentDAO.insert(orderComment);
            if(insertResult > 0) {
                return new Result<>(ResultCode.SUCCESS);
            }else {
                return new Result<>(ResultCode.MYSQL_ERROR.getCode(), "评论失败， 请稍后重试");
            }
        }else {
            return new Result<>(ResultCode.MYSQL_ERROR.getCode(), "订单状态异常");
        }
    }

    @Override
    public Result<List<OrderComment>> getOrderCommentByUserId(Integer userId, Integer page, Integer pageSize) {
        try{
            int offset = page * pageSize;
            List<OrderComment> orderCommentList = orderCommentDAO.queryByUserId(userId, offset, pageSize);
            return new Result<>(orderCommentList);
        }catch (Exception e) {
            return new Result<>(ResultCode.SERVER_ERROR.getCode(), e.toString());
        }
    }
}
