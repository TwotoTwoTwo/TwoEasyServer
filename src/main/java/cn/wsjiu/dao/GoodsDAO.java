package cn.wsjiu.dao;

import cn.wsjiu.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsDAO {
    int insert(Goods goods);
    List<Goods> queryByGoodsId(int goodsId);
    List<Goods> queryByUserId(int userId, int page, int pageSize);
    List<Goods> queryByLabel(String label, int page, int pageSize);
    int update(Goods goods);
}
