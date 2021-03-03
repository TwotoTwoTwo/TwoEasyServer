package cn.wsjiu.server.service.impl;

import cn.wsjiu.server.dao.GoodsDAO;
import cn.wsjiu.server.dao.SubscribeRecordDAO;
import cn.wsjiu.server.entity.Goods;
import cn.wsjiu.server.entity.SubscribeRecord;
import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.result.ResultCode;
import cn.wsjiu.server.service.GoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    GoodsDAO goodsDAO;

    @Resource
    SubscribeRecordDAO subscribeRecordDAO;

    @Override
    public Result<Void> publishGoods(Goods goods) {
        try{
            goodsDAO.insert(goods);
        }catch (Exception e) {
            return new Result<Void>(ResultCode.MYSQL_ERROR.getCode(),
                    "插入异常" + e.toString());
        }
        return new Result<Void>(ResultCode.SUCCESS);
    }

    @Override
    public Result<Void> editGoods(Goods goods) {
        try{
            int match = goodsDAO.update(goods);
            if(match <= 0) {
                return new Result<Void>(ResultCode.MYSQL_ERROR.getCode(),
                        "更新失败，没有匹配的物品");
            }
        }catch (Exception e) {
            return new Result<Void>(ResultCode.MYSQL_ERROR.getCode(),
                    "更新异常" + e.toString());
        }
        return new Result<Void>(ResultCode.SUCCESS);
    }

    @Override
    public Result<Void> updateGoodsState(int goodsId, int oldState, int newState) {
        try {
            int updateResult = goodsDAO.updateState(goodsId, oldState, newState);
            if(updateResult > 0) {
                return new Result<>(ResultCode.SUCCESS);
            }
            return new Result<>(ResultCode.MYSQL_ERROR);
        }catch (Exception e) {
            return new Result<>(ResultCode.MYSQL_ERROR.getCode(), e.toString());
        }
    }



    @Override
    public Result<List<Goods>> queryByGoodsId(int goodsId) {
        try{
            Set<Integer> set = new HashSet<>(1);
            set.add(goodsId);
            List<Goods> goodsList = goodsDAO.queryGoodses(set);
            if(goodsList == null || goodsList.size() == 0) {
                return new Result<List<Goods>>(ResultCode.MYSQL_ERROR.getCode(),
                        "goodsId 不存在");
            }
            return new Result<List<Goods>>(goodsList);
        }catch (Exception e) {
            return new Result<List<Goods>>(ResultCode.MYSQL_ERROR.getCode(),
                    "查询异常" + e.toString());
        }
    }

    @Override
    public Result<List<Goods>> queryByUserId(int userId, int page, int pageSize) {
        try{
            List<Goods> goodsList = goodsDAO.queryByUserId(userId, page * pageSize, pageSize);
            return new Result<List<Goods>>(goodsList);
        }catch (Exception e) {
            return new Result<List<Goods>>(ResultCode.MYSQL_ERROR.getCode(),
                    "查询异常" + e.toString());
        }
    }

    @Override
    public Result<List<Goods>> queryByLabel(String label, int page, int pageSize) {
        try{
            List<Goods> goodsList = goodsDAO.queryByLabel(label, page * pageSize, pageSize);
            return new Result<List<Goods>>(goodsList);
        }catch (Exception e) {
            return new Result<List<Goods>>(ResultCode.MYSQL_ERROR.getCode(),
                    "查询异常" + e.toString());
        }
    }

    @Override
    public Result<List<Goods>> queryAll(int page, int pageSize) {
        try{
            List<Goods> goodsList = goodsDAO.queryAll(page * pageSize, pageSize);
            return new Result<List<Goods>>(goodsList);
        }catch (Exception e) {
            return new Result<List<Goods>>(ResultCode.MYSQL_ERROR.getCode(),
                    "查询异常" + e.toString());
        }
    }

    @Override
    public Result<Map<String, Goods>> queryByGoodsId(Set<Integer> goodsIdSet) {
        List<Goods> goodsList = goodsDAO.queryGoodses(goodsIdSet);
        Map<String, Goods> map = new HashMap<>();
        if(goodsList != null && goodsList.size() != 0) {
            for(Goods goods : goodsList) {
                map.put(goods.getGoodsId().toString(), goods);
            }
        }
        return new Result<Map<String, Goods>>(map);
    }

    @Override
    public Result<Void> subscribePrice(SubscribeRecord record) {
        try{
            int res = subscribeRecordDAO.insert(record);
            if(res > 0) {
                return new Result<>(ResultCode.SUCCESS);
            }
        }catch (Exception e) {
            return new Result<>(ResultCode.MYSQL_ERROR.getCode(), e.toString());
        }
        return new Result<>(ResultCode.MYSQL_ERROR);
    }

    @Override
    public Result<Void> cancelSubscribePrice(Integer userId, Integer goodsId) {
        try{
            int res = subscribeRecordDAO.deleteByUserId(userId, goodsId);
            if(res > 0) {
                return new Result<>(ResultCode.SUCCESS);
            }
        }catch (Exception e) {
            return new Result<>(ResultCode.MYSQL_ERROR.getCode(), e.toString());
        }
        return new Result<>(ResultCode.MYSQL_ERROR);
    }

    @Override
    public Result<List<SubscribeRecord>> queryAllSubscribePriceByUserId(Integer userId) {
        try{
            List<SubscribeRecord> recordList = subscribeRecordDAO.queryByUserId(userId);
            return new Result<>(recordList);
        }catch (Exception e) {
            return new Result<>(ResultCode.MYSQL_ERROR.getCode(), e.toString());
        }
    }

    @Override
    public Result<List<Integer>> queryAllSubscribePriceByGoodsId(Integer goodsId) {
        try{
            List<Integer> userIdList = subscribeRecordDAO.queryByGoodsId(goodsId);
            return new Result<>(userIdList);
        }catch (Exception e) {
            return new Result<>(ResultCode.MYSQL_ERROR.getCode(), e.toString());
        }
    }

    @Override
    public Result<List<Goods>> searchGoodsByWord(String word, int page, int pageSize) {
        try {
            int offset = page * pageSize;
            List<Goods> goodsList = goodsDAO.searchByFullText(word, offset, pageSize);
            return new Result<>(goodsList);
        }catch (Exception e) {
            return new Result<>(ResultCode.MYSQL_ERROR.getCode(), e.toString());
        }
    }
}
