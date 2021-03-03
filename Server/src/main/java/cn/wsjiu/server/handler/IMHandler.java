package cn.wsjiu.server.handler;

import cn.wsjiu.server.entity.IM.Message;
import cn.wsjiu.server.producer.IMProducer;
import cn.wsjiu.server.result.Result;
import cn.wsjiu.server.service.GoodsService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IM即时通讯处理器，同时是rocketmq消费者
 */
@Service
public class IMHandler extends TextWebSocketHandler{
    /**
     * 消息队列聊天消息的topic
     */
    private final static String TOPIC_CHAT = "chat";

    /**
     * 路径分隔符
     */
    private final static String SLANTING_BAR = "\\";

    /**
     * 目的为用户
     */
    private final static String DESTINATION_CHAT = "chat";


    /**
     * 目的是价格（降价）通知，是广播类型
     */
    private final static String DESTINATION_PRICE = "price";


    /**
     * 用户id集合header字段的key，此字段对应的value存放用户id集合，用于广播
     */
    private final static String USER_ID_LIST_HEADER = "userIdList";

    /**
     * 保存websocket的session
     */
    private Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>(128);

    @Resource
    GoodsService goodsService;

    @Resource
    IMProducer imProducer;

    public IMHandler() {
        super();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Principal principal = null;
        principal = session.getPrincipal();
        if(principal != null && principal.getName() != null) {
            sessionMap.put(principal.getName(), session);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketmessage) throws Exception {
        //super.handleMessage(session, message);
        // 聊天信息直接推入消息队列等待处理
        String payLoad = webSocketmessage.getPayload().toString();
        imProducer.sendMessage(payLoad.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Principal principal = null;
        principal = session.getPrincipal();
        if(principal != null && principal.getName() != null) {
            sessionMap.remove(principal.getName());
        }
    }

    /**
     * 推送消息
     * @param message 消息
     * @return  true 消息推送给用户成功
     */
    public boolean sendMessage(Message message) {
        boolean successOrNot = false;
        Integer destId = message.getReceiveId();
        successOrNot = sendMessageToUser(String.valueOf(destId), message);
        return successOrNot;
    }

    /**
     * 推送消息到用户
     * @param userId 用户id
     * @param message 消息
     * @return 发送成功返回true
     */
    public boolean sendMessageToUser(String userId, Message message) {
        try {
            WebSocketSession session = sessionMap.get(userId);
            if(session != null && session.isOpen()) {
                String payLoad = JSONObject.toJSONString(message);
                WebSocketMessage<String> webSocketMessage = new TextMessage(payLoad);
                session.sendMessage(webSocketMessage);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 广播消息给某个topic内的用户
     * @param topicId 广播id
     * @param message 消息
     * @return 发送成功返回true
     */
    /*public boolean sendMessageToPriceTopic(String topicId, Message message) {
        String headerValue = message.getHeaderValue(USER_ID_LIST_HEADER);
        List<Integer> userIdList = null;
        if(headerValue == null || headerValue.length() == 0) {
            Result<List<Integer>> result = goodsService.queryAllSubscribePriceByGoodsId(Integer.parseInt(topicId));
            if(result.isSuccess()) {
                userIdList = result.getData();
            }
        }else {
            userIdList = JSONObject.parseArray(headerValue, Integer.class);
        }
        if (userIdList == null || userIdList.size() == 0) {
            return true;
        }
        Message newMessage = message.cloneWithoutHeader();
        List<Integer> consumeFailList = new ArrayList<>();
        for(Integer userId : userIdList) {
            boolean consumeResult = sendMessageToUser(String.valueOf(userId), newMessage);
            if(!consumeResult) {
                consumeFailList.add(userId);
            }
        }
        if(consumeFailList.size() > 0) {
             message.setHeader(USER_ID_LIST_HEADER, JSONObject.toJSONString(consumeFailList));
             return false;
        }
        return true;
    }*/
}
