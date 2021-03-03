package cn.wsjiu.server.dao;
import cn.wsjiu.server.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserDAO {

    /**
     * 根据用户名查询
     * @param accountName 用户名
     * @return 用户
     */
    User query(String accountName);

    /**
     * 插入用户信息
     * @param user 用户详细信息
     * @return
     */
    int insert(User user);

    /**
     * 更新用户信息
     * @param user 用户详细信息
     * @return
     */
    int update(User user);

    /**
     * 根据用户id查询用户信息
     * @param userIdSet 用户id集合
     * @return
     */
    List<User> queryUsers(Set<Integer> userIdSet);
}
