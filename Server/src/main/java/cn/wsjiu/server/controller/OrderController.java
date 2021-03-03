package cn.wsjiu.server.controller;

import cn.wsjiu.server.entity.Goods;
import cn.wsjiu.server.entity.GoodsState;
import cn.wsjiu.server.entity.Order;
import cn.wsjiu.server.entity.OrderState;
import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.result.ResultCode;
import cn.wsjiu.server.service.GoodsService;
import cn.wsjiu.server.service.OrderService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("order")
@RestController
public class OrderController {

    @Resource
    OrderService orderService;

    @Resource
    GoodsService goodsService;

    /**
     * 客户端生成订单接口
     * @param order 订单
     * @return
     */
    @RequestMapping("create")
    public Result<Void> createOrder(@RequestBody Order order) {
        int oldState = GoodsState.UNSOLD.mask;
        int newState = GoodsState.TRANSACTION.mask;
        Result<Void> updateResult = goodsService.updateGoodsState(order.getGoodsId(),
                 oldState, newState);
        if(updateResult.isSuccess()) {
            order.setState(OrderState.TRANSACTION_IN.mask);
            return orderService.createOrder(order);
        }
        return new Result<>(updateResult.getCode(), updateResult.getMsg());
    }

    /**
     * 用户取消订单
     * @param orderId 订单id
     * @param goodsId 订单中物品的id
     * @return
     */
    @RequestMapping("cancel")
    public Result<Void> cancelOrder(int orderId, int goodsId) {
        int cancelState = OrderState.TRANSACTION_CANCEL.mask;
        int inTransactionState = OrderState.TRANSACTION_IN.mask;
        Result<Void> updateOrderResult = orderService.updateOrderState(orderId,
                inTransactionState, cancelState);
        if(updateOrderResult.isSuccess()) {
            int oldState = GoodsState.TRANSACTION.mask;
            int newState = GoodsState.UNSOLD.mask;
            Result<Void> updateGoodsResult = goodsService.updateGoodsState(goodsId,
                    oldState, newState);
            if(updateGoodsResult.isSuccess()) {
                return new Result<>(ResultCode.SUCCESS);
            }
            return new Result<>(updateGoodsResult.getCode(), updateGoodsResult.getMsg());
        }
        return new Result<>(updateOrderResult.getCode(), updateOrderResult.getMsg());
    }

    /**
     * 完成订单
     * @param orderId 订单id
     * @param goodsId 订单中物品的id
     * @return
     */
    @RequestMapping("finish")
    public Result<Void> finishOrder(@RequestParam("orderId") int orderId, @RequestParam("goodsId")int goodsId) {
        int finishState = OrderState.TRANSACTION_FINISH.mask;
        int inTransactionState = OrderState.TRANSACTION_IN.mask;
        Result<Void> updateOrderResult = orderService.updateOrderState(orderId,
                inTransactionState, finishState);
        if(updateOrderResult.isSuccess()) {
            int oldState = GoodsState.TRANSACTION.mask;
            int newState = GoodsState.SOLD.mask;
            Result<Void> updateGoodsResult = goodsService.updateGoodsState(goodsId,
                    oldState, newState);
            if(updateGoodsResult.isSuccess()) {
                return new Result<>(ResultCode.SUCCESS);
            }
            return new Result<>(updateGoodsResult.getCode(), updateGoodsResult.getMsg());
        }
        return new Result<>(updateOrderResult.getCode(), updateOrderResult.getMsg());
    }

    /**
     * 根据用户id查询用户相关的订单
     * @param userId 用户id
     * @return 返回用户的所有订单
     */
    @RequestMapping("query")
    public Result<List<Order>> queryOrderByUserId(int userId) {
        return orderService.queryOrderByUserId(userId);
    }

    @RequestMapping("expressNumber")
    public Result<Void> updateOrderExpressNumber(int orderId, String expressNumber) {
        return orderService.updateExpressNumber(orderId, expressNumber);
    }
}
