package cn.wsjiu.server.dao;

import cn.wsjiu.server.entity.OrderComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderCommentDAO {
    /**
     * 插入评论
     * @param orderComment 评论数据
     * @return 大于0 表示插入成功
     */
    @Insert("insert orderComment(orderId, buyerId, sellerId, content) " +
            "values(#{orderId}, #{buyerId}, #{sellerId}, #{content})")
    int insert(OrderComment orderComment);

    /**
     * 查询用户相关的评论
     * @param userId 用户id
     * @param offset 分页偏移量
     * @param pageSize 分页大小
     * @return 分页大小的评论数据
     */
    @Select("select buyerId, time, content from orderComment " +
            "where sellerId = #{userId} limit #{offset}, #{pageSize}")
    List<OrderComment> queryByUserId(@Param("userId") Integer userId,
                                     @Param("offset") Integer offset,
                                     @Param("pageSize") Integer pageSize);
}
