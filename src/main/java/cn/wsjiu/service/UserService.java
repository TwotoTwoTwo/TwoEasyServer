package cn.wsjiu.service;

import cn.wsjiu.entity.User;
import cn.wsjiu.result.Result;
import com.alibaba.fastjson.JSONObject;

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
}
