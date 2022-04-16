package nbbang.com.nbbang.global.socket.util;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface SocketAttributeUtil {
    Long getMemberId();
    Long getPartyId();
    boolean isSubscribing();
    void subscribe(Long partyId);
    void unsubscribe();

    void setAttributeWithId(ServerHttpRequest request, Map<String, Object> attributes);
}

