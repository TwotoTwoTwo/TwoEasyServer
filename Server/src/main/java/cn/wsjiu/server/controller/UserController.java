package cn.wsjiu.server.controller;

import cn.wsjiu.server.service.ImageService;
import cn.wsjiu.server.util.StringCheckUtils;
import cn.wsjiu.server.entity.Request.LoginRequset;
import cn.wsjiu.server.entity.Request.RegisterRequest;
import cn.wsjiu.server.entity.User;
import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.result.ResultCode;
import cn.wsjiu.server.service.AuthorizeService;
import cn.wsjiu.server.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class  UserController {
    @Resource
    AuthorizeService authorizeService;

    @Resource
    UserService userService;

    /**
     * 登录接口
     * @param requset 登录校验信息
     * @return
     */
    @RequestMapping("/login")
    public String login(@RequestBody LoginRequset requset) {
        Result<User> result = null;
        if(!StringCheckUtils.checkAccountName(requset.getAccountName())) {
            result =  new Result<User>(ResultCode.ACCOUNT_ERROR);
        }
        if(!StringCheckUtils.checkPassword(requset.getPassword())) {
            result = new Result<User>(ResultCode.PASSWORD_ERROR);
        }
        if(result == null) {
            result = userService.login(requset.getAccountName(), requset.getPassword());
        }
        return JSONObject.toJSONString(result);
    }

    /**
     * 注册接口
     * @param request 注册校验接口
     * @return
     */
    @RequestMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        Result<User> result = null;
        if(!StringCheckUtils.checkAccountName(request.getAccountName())) {
            result =  new Result<User>(ResultCode.ACCOUNT_ERROR);
        }
        if(!StringCheckUtils.checkPassword(request.getPassword())) {
            result = new Result<User>(ResultCode.PASSWORD_ERROR);
        }
        if(StringUtils.isEmpty(request.getAccessToken())) {
            result = new Result<User>(ResultCode.AUTHORIZE_ERROR.getCode(), "授权凭证为空，请重新授权");
        }
        if(result == null) {
            Result<JSONObject> accessResult = authorizeService.getYiBanUserInfo(request.getAccessToken());
            if(accessResult.isSuccess()) {
                result = userService.register(request.getAccountName(),
                        request.getPassword(), accessResult.getData());
            }else {
                result = new Result<User>(accessResult.getCode(), accessResult.getMsg());
            }
        }
        return JSONObject.toJSONString(result);
    }

    @RequestMapping("/update")
    public Result<User> update(@RequestBody User user) {
        if(user.getUserId() == null) {
            return new Result<>(ResultCode.PARAM_ERROR.getCode(), "缺少用户id");
        }
        return userService.updateUser(user);
    }

    /**
     * 获取指定用户的基础信息
     * @param userIdSet 用户id集合
     * @return
     */
    @RequestMapping("/getUsersBaseInfo")
    public String getUserBaseInfo(@RequestBody Set<Integer> userIdSet) {
        Result<Map<Integer, User>> result = null;
        if(userIdSet == null || userIdSet.size() == 0) {
            result =  new Result<>(ResultCode.PARAM_ERROR);
        }else {
            result = userService.getUsers(userIdSet);
        }
        return JSONObject.toJSONString(result);
    }

    /**
     * 关注用户接口
     * @param fansId 粉丝id
     * @param followedId 关注对象id
     * @return
     */
    @RequestMapping("/follow")
    public Result<Void> follow(int fansId, int followedId) {
        if(fansId < 0 || followedId < 0) {
            return new Result<Void>(ResultCode.PARAM_ERROR);
        }
        return userService.followUser(fansId, followedId);
    }

    /**
     * 获取用户的粉丝
     * @param userId 用户id
     * @return
     */
    @RequestMapping("/fans/get")
    public Result<List<Integer>> getFans(int userId, int page, int pageSize) {
        if(userId < 0) {
            return new Result<List<Integer>>(ResultCode.PARAM_ERROR);
        }
        return userService.getFans(userId);
    }

    /**
     * 获取用户的关注
     * @param userId 用户id
     * @return
     */
    @RequestMapping("/follow/get")
    public Result<List<Integer>> getFollow(int userId) {
        if(userId < 0) {
            return new Result<List<Integer>>(ResultCode.PARAM_ERROR);
        }
        return userService.getFollow(userId);
    }

    /**
     * 取消关注用户
     * @param fansId 粉丝id
     * @param followedId 关注对象id
     * @return
     */
    @RequestMapping("/follow/cancel")
    public Result<Void> cancelFollow(int fansId, int followedId) {
        if(fansId < 0 || followedId < 0) {
            return new Result<Void>(ResultCode.PARAM_ERROR);
        }
        return userService.cancelFollow(fansId, followedId);
    }
}
