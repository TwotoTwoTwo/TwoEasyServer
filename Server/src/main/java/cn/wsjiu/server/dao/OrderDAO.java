package cn.wsjiu.server.dao;

import cn.wsjiu.server.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDAO {
    /**
     * 插入订单
     * @param order 订单
     * @return
     */
    @Insert("insert goodsOrder(sellerId, buyerId, goodsId, transactionMode,  site, state, expressNumber, phoneNumber) " +
            "values(#{sellerId}, #{buyerId}, #{goodsId}, #{transactionMode},  #{site}, #{state}, #{expressNumber}, #{phoneNumber})")
    int insert(Order order);

    /**
     * 更新订单状态
     * @param orderId 订单id
     * @param newState 新状态
     * @return
     */
    @Update("update goodsOrder set state = #{newState} where orderId = #{orderId} and state = #{oldState}")
    int updateState(@Param("orderId") int orderId,
                    @Param("oldState") int oldState,
                    @Param("newState") int newState);

    /**
     * 更新订单的快递单号
     * @param orderId 订单id
     * @param expressNumber 快递单号
     * @return
     */
    @Update("update goodsOrder set expressNumber = #{expressNumber} where orderId = #{orderId}")
    int updateExpressNumber(@Param("orderId") int orderId, @Param("expressNumber") String expressNumber);

    /**
     * 查询用户订单
     * @param userId 用户id
     * @return
     */
    @Select("select * from goodsOrder where sellerId = #{userId} or buyerId = #{userId}")
    List<Order> queryByUserId(Integer userId);
}
