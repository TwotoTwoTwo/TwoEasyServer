package cn.wsjiu.service;

import cn.wsjiu.result.Result;

import java.util.List;
import java.util.Map;

public interface ImageService {
    Result<Map<String, String>> base64ToImage (Map<String, String> base64StrMap);
}
