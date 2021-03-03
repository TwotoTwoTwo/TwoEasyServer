package cn.wsjiu.server.dao;

import cn.wsjiu.server.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

@Mapper
public interface GoodsDAO {
    /**
     * @param goods 物品信息
     * @return 返回受影响的列数，大于0说明插入正常
     */
    int insert(Goods goods);

    /**
     * 分页查询：查询用户名下的物品
     * @param userId 用户id
     * @param offset 偏移量
     * @param pageSize 页大小
     * @return 返回用户名下的物品详细信息
     */
    List<Goods> queryByUserId(@Param("userId") int userId, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 分页查询：查询某个标签下的物品
     * @param label 分类标签
     * @param offset 偏移量
     * @param pageSize 页大小
     * @return 返回某个标签下的物品详细信息
     */
    List<Goods> queryByLabel(@Param("label") String label, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 无条件查询物品
     * @param offset 偏移量
     * @param pageSize 页大小
     * @return 返回物品信息
     */
    List<Goods> queryAll(@Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 根据物品id查询物品信息
     * @param goodsIdSet 物品id集合
     * @return 返回物品信息
     */
    List<Goods> queryGoodses(Set<Integer> goodsIdSet);

    /**
     * 更新物品信息
     * @param goods 物品信息
     * @return 返回受影响的列数，大于0说明更新正常
     */
    int update(Goods goods);

    /**
     * 修改物品旧状态，必须符合旧状态才能修改为新状态
     * @param goodsId 物品id
     * @param oldState 旧状态
     * @param newState 新状态
     * @return
     */
    @Update("update goods set state = #{newState} where goodsId = #{goodsId} " +
            "and state = #{oldState}")
    int updateState(@Param("goodsId") int goodsId,
                    @Param("oldState") int oldState,
                    @Param("newState") int newState);

    /**
     * 全文索引搜索物品
     * @param word 关键字
     * @param offset 分页查询的偏移量
     * @param pageSize 分页查询的页大小
     * @return 匹配word关键字的物品
     */
    @Select("select * from goods where match(title) against(#{word}) limit #{offset}, #{pageSize}")
    List<Goods> searchByFullText(@Param("word") String word,
                                 @Param("offset") int offset,
                                 @Param("pageSize") int pageSize);
}
