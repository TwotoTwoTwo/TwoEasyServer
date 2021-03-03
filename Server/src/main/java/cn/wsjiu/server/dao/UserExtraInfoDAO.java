package cn.wsjiu.server.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wsjiu
 * 用户附加信息dao
 * 例如 关注/粉丝，等降价
 */
@Mapper
public interface UserExtraInfoDAO {
    /**
     * 插入关注记录
     * @param fansId 粉丝id
     * @param followedId 关注对象id
     * @return
     */
    int insertFollow(@Param("fansId") int fansId, @Param("followedId") int followedId);

    /**
     * 查询关注对象
     * @param fansId 粉丝id
     * @return
     */
    List<Integer> queryFollow(int fansId);

    /**
     * 查询所有粉丝
     * @param followedId 关注对象id
     * @return
     */
    List<Integer> queryFans(int followedId);

    /**
     * 取消关注
     * @param fansId 粉丝id
     * @param followedId 关注对象id
     * @return
     */
    int deleteFollow(@Param("fansId") int fansId, @Param("followedId") int followedId);
}
