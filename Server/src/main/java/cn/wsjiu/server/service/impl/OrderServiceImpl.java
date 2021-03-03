package cn.wsjiu.server.service.impl;

import cn.wsjiu.server.dao.OrderDAO;
import cn.wsjiu.server.entity.Order;
import cn.wsjiu.server.entity.OrderState;
import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.result.ResultCode;
import cn.wsjiu.server.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    OrderDAO orderDAO;

    @Override
    public Result<Void> createOrder(Order order) {
        Result<Void> result = new Result<>(ResultCode.PARAM_ERROR);
        if(order.getSellerId() == null || order.getSellerId() < 0) {
            return result;
        }
        if(order.getGoodsId() == null || order.getGoodsId() < 0) {
            return result;
        }
        if(order.getBuyerId() == null || order.getBuyerId() < 0) {
            return result;
        }
        order.setState(OrderState.TRANSACTION_IN.mask);
        try {
            int insertResult = orderDAO.insert(order);
            if(insertResult <= 0) {
                result = new Result<>(ResultCode.MYSQL_ERROR);
            }else {
                result = new Result<>(ResultCode.SUCCESS);
            }
        }catch (Exception e) {
            result = new Result<>(ResultCode.MYSQL_ERROR.getCode(), e.toString());
        }
        return result;
    }

    @Override
    public Result<Void> updateOrderState(Integer orderId, int oldState, int newState) {
        Result<Void> result;
        try {
            int updateResult = orderDAO.updateState(orderId, oldState, newState);
            if(updateResult <= 0) {
                result = new Result<>(ResultCode.MYSQL_ERROR.getCode(), "订单非交易状态");
            }else {
                result = new Result<>(ResultCode.SUCCESS);
            }
        }catch (Exception e) {
            result = new Result<>(ResultCode.MYSQL_ERROR.getCode(), e.toString());
        }
        return result;
    }

    @Override
    public Result<Void> modifyOrder(Order order) {
        return null;
    }

    @Override
    public Result<List<Order>> queryOrderByUserId(Integer userId) {
        try {
            if(userId == null || userId < 0) {
                return new Result<>(ResultCode.PARAM_ERROR);
            }
            List<Order> orderList = orderDAO.queryByUserId(userId);
            return new Result<>(orderList);
        }catch (Exception e) {
            return new Result<>(ResultCode.MYSQL_ERROR.getCode(), e.toString());
        }
    }

    @Override
    public Result<Void> updateExpressNumber(int orderId, String expressNumber) {
        int updateCount = orderDAO.updateExpressNumber(orderId, expressNumber);
        if(updateCount == 1) {
            return new Result<>(ResultCode.SUCCESS);
        }
        return new Result<>(ResultCode.MYSQL_ERROR.getCode(), "更新失败");
    }
}
