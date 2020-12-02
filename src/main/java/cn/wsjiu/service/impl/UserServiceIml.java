package cn.wsjiu.service.impl;
import cn.wsjiu.dao.UserDAO;
import cn.wsjiu.entity.User;
import cn.wsjiu.result.Result;
import cn.wsjiu.result.ResultCode;
import cn.wsjiu.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class UserServiceIml implements UserService {
    @Resource
    UserDAO userDAO;

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
            return new Result<User>(user);
        }
        return new Result<User>(ResultCode.REGISTER_ERROR);
    }

}
