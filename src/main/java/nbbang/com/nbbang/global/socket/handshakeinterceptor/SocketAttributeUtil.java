package nbbang.com.nbbang.global.socket.handshakeinterceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface SocketAttributeUtil {
    Long getMemberId();
    Object get(String key);
    void put(String key, Object value);
    boolean isSubscribing();
    void subscribe();
    void unsubscribe();

    void setAttributeWithId(ServerHttpRequest request, Map<String, Object> attributes);
}

