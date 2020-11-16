package cn.wsjiu.controller;

import cn.wsjiu.Util.StringCheckUtils;
import cn.wsjiu.entity.Request.LoginRequset;
import cn.wsjiu.entity.Request.RegisterRequest;
import cn.wsjiu.entity.User;
import cn.wsjiu.result.Result;
import cn.wsjiu.result.ResultCode;
import cn.wsjiu.service.AuthorizeService;
import cn.wsjiu.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.apache.coyote.OutputBuffer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.http.HttpRequest;

@RestController
public class UserController {
    @Resource
    AuthorizeService authorizeService;

    @Resource
    UserService userService;

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
}
