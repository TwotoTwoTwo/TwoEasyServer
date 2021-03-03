package cn.wsjiu.server.controller;

import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.service.AuthorizeService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
public class AuthorizeController {
    @Resource
    AuthorizeService authorizeService;

    @RequestMapping("/")
    public String check(){
        return "success";
    }

    @RequestMapping("/back")
    public String back(String code, String state) {
        Result<String> result = authorizeService.getAccessToken(code);
        return JSONObject.toJSONString(result);
    }
}
