package cn.wsjiu.server.service;

import cn.wsjiu.server.result.Result;

import java.util.Map;

public interface ImageService {
    /**
     * 批量转换
     * @param base64StrMap base64字符串map
     * @return 返回转换后的url
     */
    Result<Map<String, String>> batchBase64ToImage (Map<String, String> base64StrMap);

    /**
     * 将base64转换为指定路径的图片
     * @param url 图片的路径
     * @param base64Str base64字符串
     * @return 返回转换结果
     */
    Result<Void> base64ToImage(String url, String base64Str);
}
