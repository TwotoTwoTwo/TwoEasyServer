package cn.wsjiu.service.impl;

import cn.wsjiu.dao.GoodsDAO;
import cn.wsjiu.entity.Goods;
import cn.wsjiu.result.Result;
import cn.wsjiu.result.ResultCode;
import cn.wsjiu.service.GoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.DatabaseMetaData;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    GoodsDAO goodsDAO;

    @Override
    public Result<Void> publishGoods(Goods goods) {
        goods.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
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
    public Result<List<Goods>> queryByGoodsId(int goodsId) {
        try{
            List<Goods> goodsList = goodsDAO.queryByGoodsId(goodsId);
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
            List<Goods> goodsList = goodsDAO.queryByUserId(userId, page, pageSize);
            return new Result<List<Goods>>(goodsList);
        }catch (Exception e) {
            return new Result<List<Goods>>(ResultCode.MYSQL_ERROR.getCode(),
                    "查询异常" + e.toString());
        }
    }

    @Override
    public Result<List<Goods>> queryByLabel(String label, int page, int pageSize) {
        try{
            List<Goods> goodsList = goodsDAO.queryByLabel(label, page, pageSize);
            return new Result<List<Goods>>(goodsList);
        }catch (Exception e) {
            return new Result<List<Goods>>(ResultCode.MYSQL_ERROR.getCode(),
                    "查询异常" + e.toString());
        }
    }
}
