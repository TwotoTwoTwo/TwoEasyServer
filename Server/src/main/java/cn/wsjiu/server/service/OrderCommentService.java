package cn.wsjiu.server.service;

import cn.wsjiu.server.entity.OrderComment;
import cn.wsjiu.server.result.Result;

import java.util.List;

/**
 * 订单交易评论的service层接口
 * @author wsj
 */
public interface OrderCommentService {
    /**
     * 在数据库中添加一条评论
     * @param orderComment 评论
     * @return 返回插入结果
     */
    Result<Void> insertOrderComment(OrderComment orderComment);

    /**
     * 获取针对某个用户的评论
     * @param userId 用户id
     * @param page 页号
     * @param pageSize 页大小
     * @return 返回分页大小的评论数据
     */
    Result<List<OrderComment>> getOrderCommentByUserId(Integer userId, Integer page, Integer pageSize);
}
