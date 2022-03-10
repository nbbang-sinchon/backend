package nbbang.com.nbbang.global.socket;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Component
@RequiredArgsConstructor
public class LoginHandShakeInterceptor implements HandshakeInterceptor {
    private final CurrentMember currentMember;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpSession session = null;
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            session = servletRequest.getServletRequest().getSession(false);
        }
        if (session != null) {
            SecurityContext sc = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
            try {
                attributes.put("memberId", Long.parseLong(sc.getAuthentication().getPrincipal().toString()));
            } catch(Exception e) {
                attributes.put("memberId", 1L); // 개발용도
                //throw new HandshakeFailureException("Socket Handshake 실패");
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

}
