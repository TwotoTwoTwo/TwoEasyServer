package cn.wsjiu.server.service.impl;

import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.result.ResultCode;
import cn.wsjiu.server.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@Service
public class ImageServiceImpl implements ImageService {
    Base64.Decoder decoder = Base64.getDecoder();

    private final String BASE64_FORMAT = "data:image/";
    private final Character SEMICOLON = ';';
    private final String COMMA = ",";
    private final String SPRIT = "/";
    private final String PERIOD = ".";
    private final String COLON = ":";
    private final String DOUBLE_AND = "&&";
    private final String USER_ID = "userId";

    @Value("${baseUrl}")
    private String baseUrl;

    @Override
    public Result<Map<String, String>> batchBase64ToImage(Map<String, String> data) {
        String userId = data.get(USER_ID);
        if(userId == null || userId.length() == 0) {
            return new Result<Map<String, String>>(ResultCode.PARAM_ERROR.getCode(), "缺少用户id");
        }
        Map<String, String> urlMap = new HashMap<String, String>(8);
        try{
            for(String index : data.keySet()) {
                if(USER_ID.equals(index)) {
                    continue;
                }
                String base64Str = data.get(index);
                if(base64Str.indexOf(BASE64_FORMAT) != 0) {
                    return new Result<Map<String, String>>(ResultCode.BASE64_FORMAT_ERROR);
                }
                StringBuilder imgFormat = new StringBuilder(PERIOD);
                for(int i = 11; i < 20; i++) {
                    if(base64Str.charAt(i) == SEMICOLON) {
                        break;
                    }else {
                        imgFormat.append(base64Str.charAt(i));
                    }
                }
                String[] splits = base64Str.split(COMMA, 2);
                if(splits != null && splits.length < 2) {
                    return new Result<Map<String, String>>(ResultCode.BASE64_FORMAT_ERROR);
                }
                byte[] decodeResult = decoder.decode(splits[1]);
                String url = baseUrl + userId + SPRIT;
                File dir = new File(url);
                InputStream in = new ByteArrayInputStream(decodeResult);
                BufferedImage bufferedImage = ImageIO.read(in);
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                if(!dir.exists()) {
                    dir.mkdirs();
                }
                String fileName = UUID.randomUUID().toString() + DOUBLE_AND + width + COLON + height + imgFormat;
                File file = new File(url + fileName);
                while (!file.createNewFile()) {
                    fileName = UUID.randomUUID().toString() + imgFormat;
                    file = new File(url + fileName);
                }
                url = userId + SPRIT + fileName;
                OutputStream out = new FileOutputStream(file);
                out.write(decodeResult);
                out.flush();
                out.close();
                urlMap.put(index, url);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result<Map<String, String>>(urlMap);
    }


    @Override
    public Result<Void> base64ToImage(String url, String base64Str) {
        try{
            if(base64Str.indexOf(BASE64_FORMAT) != 0) {
                return new Result<Void>(ResultCode.BASE64_FORMAT_ERROR);
            }
            StringBuilder imgFormat = new StringBuilder(PERIOD);
            for(int i = 11; i < 20; i++) {
                if(base64Str.charAt(i) == SEMICOLON) {
                    break;
                }else {
                    imgFormat.append(base64Str.charAt(i));
                }
            }
            if(!url.contains(imgFormat)) {
                return new Result<>(ResultCode.BASE64_FORMAT_ERROR.getCode(), "图片格式需为png");
            }
            String[] splits = base64Str.split(COMMA, 2);
            if(splits.length < 2) {
                return new Result<Void>(ResultCode.BASE64_FORMAT_ERROR);
            }
            byte[] decodeResult = decoder.decode(splits[1]);
            File file = new File(url);
            if (!file.exists()) {
                File fileParent = file.getParentFile();
                if(!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                file.createNewFile();
            }
            OutputStream out = new FileOutputStream(file);
            out.write(decodeResult);
            out.flush();
            out.close();
            return new Result<>(ResultCode.SUCCESS);
        }catch (IOException e) {
            return new Result<>(ResultCode.SERVER_ERROR.getCode(), e.toString());
        }
    }
}
