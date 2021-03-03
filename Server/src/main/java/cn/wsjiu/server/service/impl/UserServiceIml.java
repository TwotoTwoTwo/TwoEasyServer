package cn.wsjiu.server.service.impl;
import cn.wsjiu.server.dao.UserDAO;
import cn.wsjiu.server.dao.UserExtraInfoDAO;
import cn.wsjiu.server.entity.User;
import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.result.ResultCode;
import cn.wsjiu.server.service.ImageService;
import cn.wsjiu.server.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class UserServiceIml implements UserService {
    @Value("${baseUrl}")
    private String baseUrl;

    private final String SPRIT = "/";

    /**
     * 用户头像的图片名
     */
    private final String HEAD_IMAGE_FILE_NAME = "head.png";

    @Resource
    UserDAO userDAO;

    @Resource
    UserExtraInfoDAO userExtraInfoDAO;

    @Resource
    ImageService imageService;

    @Override
    public Result<User> login(String accountName, String password) {
        User user = userDAO.query(accountName);
        if(user != null) {
            if(password.equals(user.getPassword())) {
                user.setPassword(null);
                return new Result<User>(user);
            }else {
                return new Result<User>(ResultCode.PASSWORD_ERROR);
            }
        }
        return new Result<User>(ResultCode.ACCOUNT_ERROR);
    }

    private final String YB_USER_ID = "yb_userid";
    private final String YB_USER_NAME = "yb_username";
    private final String YB_USER_NICK_NAME = "yb_usernick";
    private final String YB_SEX = "yb_sex";
    private final String YB_USER_HEAD = "yb_userhead";
    private final String YB_SCHOOL_ID = "yb_schoolid";
    private final String YB_SCHOOL_NAME = "yb_schoolname";

    @Override
    public Result<User> register(String accountName, String password, JSONObject userInfoJSONObject) {
        User user = new User();
        user.setAccountName(accountName);
        user.setPassword(password);
        user.setHeadUrl(userInfoJSONObject.getString(YB_USER_HEAD));
        user.setSchoolId(userInfoJSONObject.getString(YB_SCHOOL_ID));
        user.setSchoolName(userInfoJSONObject.getString(YB_SCHOOL_NAME));
        user.setSex(userInfoJSONObject.getString(YB_SEX));
        user.setUserName(userInfoJSONObject.getString(YB_USER_NAME));
        user.setUserNickName(userInfoJSONObject.getString(YB_USER_NICK_NAME));
        user.setYibanId(userInfoJSONObject.getString(YB_USER_ID));
        int userId = userDAO.insert(user);
        if(userId > 0) {
            user.setUserId(userId);
            user.setPassword(null);
            return new Result<User>(user);
        }
        return new Result<User>(ResultCode.REGISTER_ERROR);
    }

    @Override
    public Result<User> updateUser(User user) {
        if(user.getHeadUrl() != null) {
            String base64Str = user.getHeadUrl();
            String url = user.getUserId() + SPRIT + HEAD_IMAGE_FILE_NAME;
            Result<Void> changeResult = imageService.base64ToImage(baseUrl + url, base64Str);
            if(changeResult.isSuccess()) {
                user.setHeadUrl(url);
            }else {
                user.setHeadUrl(null);
            }
        }
        int effectiveCount = userDAO.update(user);
        if(effectiveCount < 1) {
            return new Result<>(ResultCode.MYSQL_ERROR);
        }
        return new Result<>(user);
    }

    @Override
    public Result<Map<Integer, User>> getUsers(Set<Integer> userIdSet) {
        Result<Map<Integer, User>> result = null;
        try{
            List<User> userList = userDAO.queryUsers(userIdSet);
            Map<Integer, User> map = new HashMap<>(userList.size());
            for(User user : userList) {
                map.put(user.getUserId(), user);
            }
            result = new Result<>(map);
        }catch (Exception e) {
            result = new Result<>(ResultCode.MYSQL_ERROR.getCode(), e.toString());
        }
        return result;
    }

    @Override
    public Result<Void> followUser(int fansId, int followedId) {
        Result<Void> result;
        try{
            int insertResult = userExtraInfoDAO.insertFollow(fansId, followedId);
            if(insertResult > 0) {
                result = new Result<>(ResultCode.SUCCESS);
            }else {
                result = new Result<>(ResultCode.MYSQL_ERROR.getCode(), "数据库关注失败");
            }
        }catch (Exception e) {
            result = new Result<>(ResultCode.MYSQL_ERROR.getCode(), e.toString());
        }
        return result;
    }

    @Override
    public Result<List<Integer>> getFans(int followedId) {
        Result<List<Integer>> result;
        try {
            List<Integer> fansIdlist = userExtraInfoDAO.queryFans(followedId);
            result = new Result<>(fansIdlist);
        }catch (Exception e) {
            result = new Result<>(ResultCode.MYSQL_ERROR.getCode(), e.toString());
        }
        return result;
    }

    @Override
    public Result<List<Integer>> getFollow(int fansId) {
        Result<List<Integer>> result;
        try {
            List<Integer> followedIdlist = userExtraInfoDAO.queryFollow(fansId);
            result = new Result<>(followedIdlist);
        }catch (Exception e) {
            result = new Result<>(ResultCode.MYSQL_ERROR.getCode(), e.toString());
        }
        return result;
    }

    @Override
    public Result<Void> cancelFollow(int fansId, int followedId) {
        Result<Void> result;
        try{
            int deleteResult = userExtraInfoDAO.deleteFollow(fansId, followedId);
            if(deleteResult > 0) {
                result = new Result<>(ResultCode.SUCCESS);
            }else {
                result = new Result<>(ResultCode.MYSQL_ERROR.getCode(), "取消关注失败");
            }
        }catch (Exception e) {
            result = new Result<>(ResultCode.MYSQL_ERROR.getCode(), e.toString());
        }
        return result;
    }
}
