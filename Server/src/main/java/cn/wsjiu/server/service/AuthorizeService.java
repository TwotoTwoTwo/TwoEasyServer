package cn.wsjiu.server.service;

import cn.wsjiu.server.result.Result;
import com.alibaba.fastjson.JSONObject;

public interface AuthorizeService {
    /**
     * @param code authorize code
     * @return return user's accessToken
     */
    Result<String> getAccessToken(String code);

    /**
     * @param accessToken str which can use yiban api from user
     * @return return some infomation about yiban user
     */
    Result<JSONObject> getYiBanUserInfo(String accessToken);
}
