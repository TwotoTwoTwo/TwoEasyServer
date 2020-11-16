package cn.wsjiu.service;

import cn.wsjiu.entity.Goods;
import cn.wsjiu.result.Result;

import java.util.List;

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
}
