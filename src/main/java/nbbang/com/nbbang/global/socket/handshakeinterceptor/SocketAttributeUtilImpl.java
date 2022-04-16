package nbbang.com.nbbang.global.socket.handshakeinterceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static nbbang.com.nbbang.global.error.GlobalErrorResponseMessage.AUTHENTICATION_ERROR;
import static nbbang.com.nbbang.global.socket.ChatRoomSubscribeStatus.SUBSCRIBE;
import static nbbang.com.nbbang.global.socket.ChatRoomSubscribeStatus.UNSUBSCRIBE;

@Component
public class SocketAttributeUtilImpl implements SocketAttributeUtil {
    private String attrMemberId = "memberId";
    private String attrPartyId = "partyId";
    private String attrStatus = "status";

    private Map<String, Object> attributes = new HashMap<>();

    @Override
    public Long getMemberId() {
        return Long.valueOf(attributes.get(attrMemberId).toString());
    }

    @Override
    public Long getPartyId() {
        return Long.valueOf(attributes.get(attrPartyId).toString());
    }

    @Override
    public boolean isSubscribing() {
        return attributes.getOrDefault(attrStatus, UNSUBSCRIBE).equals(SUBSCRIBE);
    }

    @Override
    public void subscribe(Long partyId) {
        attributes.put(attrStatus, SUBSCRIBE);
        attributes.put(attrPartyId, partyId);
    }

    @Override
    public void unsubscribe() {
        attributes.put(attrStatus, UNSUBSCRIBE);
        attributes.put(attrPartyId, null);
    }

    @Override
    public void setAttributeWithId(ServerHttpRequest request, Map<String, Object> attributes) {
        try {
            this.attributes = attributes;
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            this.attributes.put(attrMemberId, servletRequest.getPrincipal().getName());
        } catch (Exception e) {
            throw new IllegalStateException(AUTHENTICATION_ERROR);
        }
    }
}
