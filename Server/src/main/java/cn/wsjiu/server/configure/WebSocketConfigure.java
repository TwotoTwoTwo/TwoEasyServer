package cn.wsjiu.server.configure;

import cn.wsjiu.server.handler.IMHandler;
import cn.wsjiu.server.handler.UserHandShakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import javax.annotation.Resource;

@Configuration
@EnableWebSocket
public class WebSocketConfigure implements WebSocketConfigurer {
    @Resource
    IMHandler imHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(imHandler, "webSocket")
                .setHandshakeHandler(new UserHandShakeHandler())
                .setAllowedOrigins("*");
    }
}
