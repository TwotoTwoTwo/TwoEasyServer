package cn.wsjiu.server.dao;

import cn.wsjiu.server.entity.SubscribeRecord;
import org.apache.ibatis.annotations.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@Mapper
public interface SubscribeRecordDAO {
     /**
      * 根据物品id查询所有对此物品订阅了降价通知用户id
      * @param goodsId 物品id
      * @return 用户id
      */
     @Select("select userId from subscribeRecord where goodsId = #{goodsId}")
     List<Integer> queryByGoodsId(Integer goodsId);

     /**
      * 查询用户所有订阅记录
      * @param userId 用户id
      * @return 所有的物品订阅记录
      */
     @Select("select * from subscribeRecord where userId = #{userId}")
     @ResultType(SubscribeRecord.class)
     List<SubscribeRecord> queryByUserId(Integer userId);

     /**
      * 插入一条订阅降价通知的记录
      * @param record 订阅记录
      * @return
      */
     @Insert("insert SubscribeRecord(goodsId, userId, subscribePrice) values(#{goodsId}, #{userId}, #{subscribePrice})")
     int insert(SubscribeRecord record);

     /**
      * 删除物品的所有相关降价订阅记录
      * @param goodsId 物品
      * @return
      */
     @Delete("delete from SubscribeRecord where goodsId = #{goodsId}")
     int deleteAllByGoodsId(Integer goodsId);

     /**
      * 删除用户的某条降价订阅记录
      * @param userId 用户id
      * @param goodsId 物品id
      * @return
      */
     @Delete("delete from SubscribeRecord where userId = #{userId} and goodsId = #{goodsId}")
     int deleteByUserId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
}
