package cn.wsjiu.server.service;

import cn.wsjiu.server.entity.Goods;
import cn.wsjiu.server.entity.SubscribeRecord;
import cn.wsjiu.server.result.Result;

import java.util.List;
import java.util.Set;
import java.util.*;

/**
 * @author wsjiu
 * @date 2020.11.16
 */
public interface GoodsService {
    /**
     * 发布二手物品
     * @param goods 物品详细信息
     * @return
     */
    Result<Void> publishGoods(Goods goods);

    /**
     * 编辑物品，修改相关信息
     * @param goods 物品需要修改的具体信息
     * @return
     */
    Result<Void> editGoods(Goods goods);

    /**
     * 更新物品状态
     * @param goodsId 物品id
     * @param oldState  旧状态
     * @param newState 新状态
     * @return
     */
    Result<Void> updateGoodsState(int goodsId, int oldState, int newState);

    /**
     * 查询物品
     * @param goodsId 物品唯一id
     * @return 返回goodsId对应的物品
     */
    Result<List<Goods>> queryByGoodsId(int goodsId);

    /**
     * 通过用户id查询用户帐号下的所有物品
     * @param userId 用户id
     * @param page 页号
     * @param pageSize 页大小
     * @return
     */
    Result<List<Goods>> queryByUserId(int userId, int page, int pageSize);

    /**
     * 通过标签查询某个类别的物品
     * @param label 标签
     * @param page 页号
     * @param pageSize 页大小
     * @return 返回该类别的goods的List
     */
    Result<List<Goods>> queryByLabel(String label, int page, int pageSize);

    /**
     * 无条件查询任意物品
     * @param page 页号
     * @param pageSize 页大小
     * @return
     */
    Result<List<Goods>> queryAll(int page, int pageSize);

    /**
     * @param goodsIdSet
     * @return 获取指定id的物品
     */
    Result<Map<String, Goods>>  queryByGoodsId(Set<Integer> goodsIdSet);

    /**
     * 订阅物品价格变化
     * @param record 订阅记录
     * @return
     */
    Result<Void> subscribePrice(SubscribeRecord record);

    /**
     * 取消订阅物品价格变化
     * @param userId 用户id
     * @param goodsId 物品id
     * @return
     */
    Result<Void> cancelSubscribePrice(Integer userId, Integer goodsId);

    /**
     * 查询用户订阅的所有物品
     * @param userId 用户id
     * @return
     */
    Result<List<SubscribeRecord>> queryAllSubscribePriceByUserId(Integer userId);

    /**
     * 查询用户订阅的所有物品
     * @param goodsId 用户id
     * @return
     */
    Result<List<Integer>> queryAllSubscribePriceByGoodsId(Integer goodsId);

    /**
     * 根据关键字搜索物品
     * @param word 关键字
     * @param page 页号
     * @param pageSize 页大小
     * @return 符合搜索条件的物品
     */
    Result<List<Goods>> searchGoodsByWord(String word, int page, int pageSize);
}
