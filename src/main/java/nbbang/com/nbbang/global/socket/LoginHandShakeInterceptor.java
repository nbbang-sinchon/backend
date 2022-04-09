package nbbang.com.nbbang.global.socket;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.security.context.CurrentMember;
import nbbang.com.nbbang.global.security.context.SocketIdUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoginHandShakeInterceptor implements HandshakeInterceptor {

    private final SocketIdUtil socketIdUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        socketIdUtil.rememberIdSocket(request, attributes);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

}