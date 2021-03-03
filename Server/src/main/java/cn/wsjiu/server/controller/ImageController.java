package cn.wsjiu.server.controller;

import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Resource
    ImageService imageService;

    @RequestMapping("/upload")
    public Result<Map<String, String>> base64ToImg(@RequestBody Map<String, String> base64StrMap) {
        if (base64StrMap == null || base64StrMap.size() == 0) {
            return new Result<Map<String, String>>(new HashMap<String, String>());
        }
        return imageService.batchBase64ToImage(base64StrMap);
    }

    @Value("${baseUrl}")
    private String baseUrl;

    @RequestMapping(value = "/get/{userId}/{imgName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getImage(@PathVariable String userId, @PathVariable String imgName) {
        String path =   baseUrl + userId + "/" + imgName;
        try {
            InputStream in = new FileInputStream(new File(path));
            byte[] data = new byte[in.available()];
            in.read(data,0, data.length);
            in.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/delete/{userId}/{imgName}", produces = MediaType.IMAGE_PNG_VALUE)
    public void deleteImage(@PathVariable String userId, @PathVariable String imgName) {
        String path =   baseUrl + userId + "/" + imgName;
        File file = new File(path);
        file.deleteOnExit();
    }
}
