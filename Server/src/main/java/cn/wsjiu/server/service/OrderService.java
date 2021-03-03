package cn.wsjiu.server.service;

import cn.wsjiu.server.entity.Order;
import cn.wsjiu.server.result.Result;

import java.util.List;

public interface OrderService {
    /**
     * 生成订单
     * @param order 订单
     * @return 生成结果
     */
    Result<Void> createOrder(Order order);

    /**
     * 更新订单状态
     * @param orderId 订单id
     * @param oldState 旧订单状态
     * @param newState 新订单状态
     * @return 取消结果
     */
    Result<Void> updateOrderState(Integer orderId, int oldState, int newState);

    /**
     * 修改订单
     * @param order 新订单
     * @return 修改结果
      */
    Result<Void> modifyOrder(Order order);

    /**
     * 查询用户所有订单
     * @param userId 用户id
     * @return 查询结果
     */
    Result<List<Order>> queryOrderByUserId(Integer userId);

    /**
     * 更新订单的快递单号
     * @param orderId 订单id
     * @param expressNumber 快递单号
     * @return 返回更新结果
     */
    Result<Void> updateExpressNumber(int orderId, String expressNumber);

}
