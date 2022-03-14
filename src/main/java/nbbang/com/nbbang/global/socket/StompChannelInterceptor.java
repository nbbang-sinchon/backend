
package nbbang.com.nbbang.global.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.validator.PartyMemberValidatorById;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
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

    private final ChatRoomService chatRoomService;
    private final PartyMemberValidatorById partyMemberValidatorById;

    private static final String TOPIC_GLOBAL = "/topic/global";
    private static final String TOPIC_CHATTING = "/topic/chatting";
    private static final String TOPIC_BREAD_BOARD = "/topic/breadBoard";

    private Long memberId(Message<?> message) {
        Map<String, Object> sessionHeaders = SimpMessageHeaderAccessor.getSessionAttributes(message.getHeaders());
        return Long.parseLong(sessionHeaders.get("memberId").toString());
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Map<String, Object> attributes = accessor.getSessionAttributes();

        attributes.put("memberId", memberId(message));
        Long memberId = (Long) attributes.get("memberId");
        log.info("[{}] message: {}", accessor.getCommand(), message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            connect(attributes);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String destination = accessor.getDestination();
            if(destination.startsWith(TOPIC_GLOBAL))  {
                Long globalMemberId = Long.valueOf(destination.substring(14));
                if(memberId!=globalMemberId){throw new RuntimeException("자신의 소켓만 구독할 수 있습니다. ");}
            }
            else if(destination.startsWith(TOPIC_CHATTING)){
                Long partyId = Long.valueOf(destination.substring(16));
                partyMemberValidatorById.isPartyMember(partyId, memberId);
                chatRoomService.enter(attributes, partyId);
            }else if(destination.startsWith(TOPIC_BREAD_BOARD)){
                Long partyId = Long.valueOf(destination.substring(18));
                partyMemberValidatorById.isPartyMember(partyId, memberId);
            }
            else{
                throw new IllegalArgumentException("올바른 토픽을 입력해주세요.");
            }
        } else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) {
            String destination = accessor.getSubscriptionId();
            if(destination.startsWith(TOPIC_CHATTING)){
                Long partyId = Long.valueOf(destination.substring(16));
                chatRoomService.exit(attributes, partyId);
            }
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            chatRoomService.exitIfSubscribing(attributes);
        }
        return message;
    }

    public void connect(Map<String, Object> attributes) {
        attributes.put("status", "none");
    }
}
