package cn.wsjiu.server.handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import java.security.Principal;
import java.util.Map;

public class  UserHandShakeHandler extends DefaultHandshakeHandler {
    /**
     * 用户id
     */
    private String USER_ID = "userId";

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        ServletServerHttpRequest httpRequest = (ServletServerHttpRequest) request;
        String userId = httpRequest.getServletRequest().getHeader(USER_ID);
        if(userId == null || userId.length() == 0) return null;
        return new UserPrincipal(userId);
    }

    class UserPrincipal implements Principal {
        /**
         *  使用唯一的userId作为websocket的principal
         */
        private String userId;

        public UserPrincipal(String userId) {
            this.userId = userId;
        }

        @Override
        public String getName() {
            return userId;
        }
    }

}
