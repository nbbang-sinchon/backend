package nbbang.com.nbbang.global.security.context;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface SocketIdUtil {
    Long idFromSocket(Message<?> message);
    void rememberIdSocket(ServerHttpRequest request, Map<String, Object> attributes);
}
