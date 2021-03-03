package cn.wsjiu.server.service;

import cn.wsjiu.server.entity.User;
import cn.wsjiu.server.result.Result;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService {
    /**
     * @param accountName
     * @param password
     * @return the account's user information
     */
    Result<User> login(String accountName, String password);

    /**
     * @param accountName
     * @param password
     * @param userInfoJSONObject
     * @return initial user information
     */
    Result<User> register(String accountName, String password, JSONObject userInfoJSONObject);

    /**
     * 修改用户信息
     * @param user 用户需要修改的信息
     * @return 修改结果
     */
    Result<User> updateUser(User user);

    /**
     * 批量获取多个用户的基础信息
     * @param userIdSet 需要获取的用户信息的id
     * @return 返回对应的user实体，key为userid，userId不存在时为null
     */
    Result<Map<Integer, User>> getUsers(Set<Integer> userIdSet);

    /**
     * 关注某位用户
     * @param fansId 粉丝id
     * @param followedId 关注对象id
     * @return
     */
    Result<Void> followUser(int fansId, int followedId);

    /**
     * 分页获取用户粉丝的id
     * @param followedId 关注对象id
     * @return
     */
    Result<List<Integer>> getFans(int followedId);

    /**
     * 获取用户作为粉丝的所有关注对象
     * @param fansId 粉丝id
     * @return
     */
    Result<List<Integer>> getFollow(int fansId);

    /**
     * 取消对某个用户的关注
     *  @param fansId 粉丝id
     *  @param followedId 关注对象id
     * @return
     */
    Result<Void> cancelFollow(int fansId, int followedId);
}
