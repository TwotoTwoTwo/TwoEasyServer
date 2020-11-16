package cn.wsjiu.service.impl;

import cn.wsjiu.result.Result;
import cn.wsjiu.result.ResultCode;
import cn.wsjiu.service.AuthorizeService;
import cn.yiban.open.Authorize;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {
    /**
     * the only id and secret by yiban
     */
    private final static String APP_ID = "ec8ae1a7c85a8633";
    private final static String APP_SECRET = "b17ab2592324ab1bfd116b7b070f1596";
    private final static String BACK_URL = "http://www.wsjiu.com:8080/back";
    private HttpURLConnection httpURLConnection;

    public Result<String> getAccessToken(String code) {
        Authorize authorize = new Authorize(APP_ID, APP_SECRET);
        String accessToken = authorize.querytoken(code, BACK_URL);
        if(StringUtils.isEmpty(accessToken)) {
            return new Result<String>(ResultCode.AUTHORIZE_ERROR);
        }
        return new Result<String>(accessToken);
    }

    private final String USER_INFO_API = "https://openapi.yiban.cn/user/me?access_token=";
    private final String METHOD = "GET";
    private final String STATUS = "status";
    private final String INFO = "info";
    private final String SUCCESS = "success";
    private final String ERROR_MSG_CN = "msgCN";
    public Result<JSONObject> getYiBanUserInfo(String accessToken) {
        try {
            URL url = new URL(USER_INFO_API + accessToken);
            HttpURLConnection httpURLConnection = (HttpURLConnection)(url.openConnection());
            httpURLConnection.setRequestMethod(METHOD);
            InputStream in = httpURLConnection.getInputStream();
            OutputStream out = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            in.close();
            String responseStr = out.toString();
            out.close();
            JSONObject responseJSONObject = JSONObject.parseObject(responseStr);
            if(SUCCESS.equals(responseJSONObject.getString(STATUS))) {
                return new Result<JSONObject>(responseJSONObject.getJSONObject(INFO));
            }else {
                JSONObject errorInfoJSONObject = responseJSONObject.getJSONObject(INFO);
                return new Result<JSONObject>(ResultCode.AUTHORIZE_ERROR.getCode(), errorInfoJSONObject.getString(ERROR_MSG_CN));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result<JSONObject>(ResultCode.SERRVER_ERROR.getCode(), e.toString());
        }
    }
}
