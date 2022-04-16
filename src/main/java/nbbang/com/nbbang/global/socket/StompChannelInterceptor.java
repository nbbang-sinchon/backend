
package nbbang.com.nbbang.global.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.socket.handshakeinterceptor.SocketAttributeUtil;
import nbbang.com.nbbang.global.socket.service.SocketChatRoomService;
import nbbang.com.nbbang.global.validator.PartyMemberValidator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

// https://daddyprogrammer.org/post/5290/spring-websocket-chatting-server-enter-qut-event-view-user-count/
// https://stackoverflow.com/questions/44852776/spring-mvc-websockets-with-stomp-authenticate-against-specific-channels
@Slf4j
@Component
@RequiredArgsConstructor
public class StompChannelInterceptor implements ChannelInterceptor {

    private final SocketChatRoomService socketChatRoomService;
    private final PartyMemberValidator partyMemberValidator;
    private final SocketAttributeUtil socketAttributeUtil;

    private static final String TOPIC_GLOBAL = "/topic/global";
    private static final String TOPIC_CHATTING = "/topic/chatting";
    private static final String TOPIC_BREAD_BOARD = "/topic/breadBoard";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("[{}] message: {}", accessor.getCommand(), message);

        switch(accessor.getCommand()) {
            case CONNECT:
                break;
            case SUBSCRIBE:
                subscribe(accessor.getDestination());
                break;
            case UNSUBSCRIBE:
                unsubscribe(accessor.getDestination());
                break;
            case DISCONNECT:
                disconnect();
                break;
            default:
                throw new IllegalArgumentException("올바른 토픽을 입력해주세요");
        }
        return message;
    }

    private void subscribe(String destination) {
        Long socketMemberId = socketAttributeUtil.getMemberId();
        if (destination.startsWith(TOPIC_GLOBAL)) {
            Long globalMemberId = Long.valueOf(destination.substring(14));
            if (socketMemberId != globalMemberId) {
                throw new RuntimeException("자신의 소켓만 구독할 수 있습니다. ");
            }
        } else if (destination.startsWith(TOPIC_CHATTING)) {
            Long partyId = Long.valueOf(destination.substring(16));
            partyMemberValidator.validatePartyMember(partyId, socketMemberId);
            socketAttributeUtil.put("partyId", partyId);
            socketAttributeUtil.subscribe(partyId);
            socketChatRoomService.enter(partyId, socketMemberId);
        } else if (destination.startsWith(TOPIC_BREAD_BOARD)) {
            Long partyId = Long.valueOf(destination.substring(18));
            partyMemberValidator.validatePartyMember(partyId, socketMemberId);
        }
    }
    private void unsubscribe(String destination) {
        if (destination.startsWith(TOPIC_CHATTING)) {
            Long partyId = Long.valueOf(destination.substring(16));
            socketExit(partyId);
        }
    }
    private void disconnect() {
        if(socketAttributeUtil.isSubscribing()){
            Long partyId = (Long) socketAttributeUtil.get("partyId");
            socketExit(partyId);
        }
    }

    private void socketExit(Long partyId){
        Long memberId = socketAttributeUtil.getMemberId();
        socketChatRoomService.exit(memberId, partyId);
        socketAttributeUtil.unsubscribe();
    }
}
