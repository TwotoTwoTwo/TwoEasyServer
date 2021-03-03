package cn.wsjiu.server.controller;

import cn.wsjiu.server.agora.RtcTokenBuilder;
import cn.wsjiu.server.entity.Goods;
import cn.wsjiu.server.entity.IM.Message;
import cn.wsjiu.server.entity.Order;
import cn.wsjiu.server.entity.SubscribeRecord;
import cn.wsjiu.server.entity.User;
import cn.wsjiu.server.handler.IMHandler;
import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.result.ResultCode;
import cn.wsjiu.server.service.GoodsService;
import cn.wsjiu.server.service.OrderService;
import cn.wsjiu.server.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;

/**
 * 应用的通用接口
 * @author wsj
 */
@RestController
@RequestMapping("app")
public class AppController {
    @Resource
    private GoodsService goodsService;
    @Resource
    private UserService userService;
    @Resource
    private OrderService orderService;

    @Resource
    private IMHandler imHandler;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 订阅记录在map中的key
     */
    private final String SUBSCRIBE_RECORD_KEY = "subscribeRecord";

    /**
     * 物品在map中的key
     */
    private final String COMMON_GOODS_KEY = "commonGoods";

    /**
     * 用户信息在map中的key
     */
    private final String USER_KEY = "user";

    /**
     * 发布物品记录在map中的key
     */
    private final String PUBLISH_GOODS_KEY = "publishGoods";

    /**
     * 订单记录在map中的key
     */
    private final String ORDER_KEY = "order";

    private final String FOLLOWED_KEY = "followedId";

    /**
     * 应用启动时调用的接口，返回应用信息
     * @return 用户的一些个人信息，发布的物品及订阅信息等
     */
    @RequestMapping("start")
    public Result<Map<String, Object>> start(Integer userId) {
        //TODO 订单信息
        Map<String, Object> resultMap = new HashMap<>(4);
        Result<List<SubscribeRecord>> subscribeResult = goodsService.queryAllSubscribePriceByUserId(userId);
        Set<Integer> goodsIdSet = new HashSet<>();
        if(subscribeResult.isSuccess()) {
            if(subscribeResult.getData() != null && subscribeResult.getData().size() > 0) {
                resultMap.put(SUBSCRIBE_RECORD_KEY, subscribeResult.getData());
                for(SubscribeRecord subscribeRecord : subscribeResult.getData()) {
                    goodsIdSet.add(subscribeRecord.getGoodsId());
                }
            }
        }else {
            return new Result<>(subscribeResult.getCode(), subscribeResult.getMsg());
        }
        Result<List<Order>> queryOrderResult = orderService.queryOrderByUserId(userId);
        if(queryOrderResult.isSuccess()) {
            List<Order> orderList = queryOrderResult.getData();
            if(orderList != null) {
                resultMap.put(ORDER_KEY, orderList);
                for (Order order : orderList
                     ) {
                    goodsIdSet.add(order.getGoodsId());
                }
            }
        }
        Set<Integer> userIdSet = new HashSet<>(goodsIdSet.size());
        if(goodsIdSet.size() > 0) {
            Result<Map<String, Goods>> goodsResult = goodsService.queryByGoodsId(goodsIdSet);
            if(goodsResult.isSuccess()) {
                if(goodsResult.getData() != null && goodsResult.getData().size() > 0) {
                    resultMap.put(COMMON_GOODS_KEY, goodsResult.getData());
                    for (Goods goods : goodsResult.getData().values()
                         ) {
                        userIdSet.add(goods.getUserId());
                    }
                }
            }else {
                return new Result<>(goodsResult.getCode(), goodsResult.getMsg());
            }
        }
        if(userIdSet.size() > 0) {
            Result<Map<Integer, User>> userResult = userService.getUsers(userIdSet);
            if(userResult.isSuccess()) {
                resultMap.put(USER_KEY, userResult.getData());
            }else {
                return new Result<>(userResult.getCode(), userResult.getMsg());
            }
        }
        Result<List<Goods>> publishGoodsResult = goodsService.queryByUserId(userId, 0, 10);
        if(publishGoodsResult.isSuccess()) {
            resultMap.put(PUBLISH_GOODS_KEY, publishGoodsResult.getData());
        }else {
            return new Result<>(publishGoodsResult.getCode(), publishGoodsResult.getMsg());
        }
        Result<List<Integer>> followedResult = userService.getFollow(userId);
        if(followedResult.isSuccess()) {
            resultMap.put(FOLLOWED_KEY, followedResult.getData());
        }
        return new Result<>(resultMap);
    }

    /**
     * 声网平台注册的app id
     */
    private static final String AGORA_APP_ID = "e33923f1771a49e5bd6567f75fbb965e";
    /**
     * 声网平台注册的app证书
     */
    private static final String AGORA_APP_CERTIFICATE = "94b1913fffe5429a99ebe1e0ed0a5f40";

    /**
     *  声网视频通信时的token有效期,单位秒
     */
    private static final int AGORA_TOKEN_EXPIRE_TIME = 3600;

    /**
     * agora通信的channelName在redis中的有效期
     */
    private static final long AGORA_CHANNEL_NAME_EXPIRE_TIME = 10;

    private static final String AGORA_CHANNEL_NAME_KEY = "channelName";
    private static final String AGORA_TOKEN_KEY = "token";

    private static final String COLON = ":";

    /**
     * 卖家发起开眼呼叫，邀请买家进行开眼
     * @param message 消息
     * @return 成功返回此次连接的鉴权token, 失败则为目标用户已经 离线
     */
    @RequestMapping("openEye/call")
    public Result<Map<String, String>> call(@RequestBody Message message) {
        // 存在漏洞，应该再加一个随机数防止channelName固化
        String channelName = message.getChatId();
        String targetUserIdStr = String.valueOf(message.getReceiveId());
        boolean isSuccess = imHandler.sendMessageToUser(targetUserIdStr, message);
        if(isSuccess) {
            redisTemplate.opsForValue().set(String.valueOf(message.getChatId()),
                    channelName);
            redisTemplate.expire(String.valueOf(message.getChatId()), AGORA_CHANNEL_NAME_EXPIRE_TIME, TimeUnit.SECONDS);
            int timestamp = (int)(System.currentTimeMillis() / 1000 + AGORA_TOKEN_EXPIRE_TIME);
            RtcTokenBuilder builder = new RtcTokenBuilder();
            String token = builder.buildTokenWithUid(AGORA_APP_ID, AGORA_APP_CERTIFICATE, channelName,
                    message.getSendId(), RtcTokenBuilder.Role.Role_Publisher, timestamp);
            Map<String, String> resultMap = new HashMap<>(2);
            resultMap.put(AGORA_CHANNEL_NAME_KEY, channelName);
            resultMap.put(AGORA_TOKEN_KEY, token);
            return new Result<>(resultMap);
        }else {
            return new Result<>(ResultCode.USER_OFFLINE_ERROR);
        }
    }



    /**
     * 买家进行开眼连接，生成agora sdk的token返回
     * @param chatId 买家卖家之间的聊天id，具备唯一性
     * @param userId 买家id
     * @return
     */
    @RequestMapping("openEye/connect")
    public Result<Map<String, String>> connect(String chatId, Integer userId) {
        int timestamp = (int)(System.currentTimeMillis() / 1000 + AGORA_TOKEN_EXPIRE_TIME);
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        String channelName = redisTemplate.opsForValue().get(chatId);
        if(channelName != null && channelName.length() > 0) {
            RtcTokenBuilder builder = new RtcTokenBuilder();
            String token = builder.buildTokenWithUid(AGORA_APP_ID, AGORA_APP_CERTIFICATE, channelName,
                    userId, RtcTokenBuilder.Role.Role_Attendee, timestamp);
            Map<String, String> resultMap = new HashMap<>(2);
            resultMap.put(AGORA_CHANNEL_NAME_KEY, channelName);
            resultMap.put(AGORA_TOKEN_KEY, token);
            return new Result<>(resultMap);
        }
        return new Result<>(ResultCode.BUYER_OPEN_EYE_ERROR);
    }

    /**
     * 取消连接(包括未接通取消和进行中取消)
     * @param channelName 频道名
     * @param userId 用户名
     * @return 返回发送成功与否
     */
    @RequestMapping("video/cancel")
    public Result<Void> cancelCall(String channelName, Integer userId) {
        return new Result<>(ResultCode.SUCCESS);
    }
}
