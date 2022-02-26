
package nbbang.com.nbbang.global.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.dto.ChatReadSocketDto;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.chat.service.ChatSessionService;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

// https://daddyprogrammer.org/post/5290/spring-websocket-chatting-server-enter-qut-event-view-user-count/
// https://stackoverflow.com/questions/44852776/spring-mvc-websockets-with-stomp-authenticate-against-specific-channels
@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final ChatService chatService;
    private final PartyService partyService;
    private final ChatSessionService chatSessionService;
    private final PartyMemberService partyMemberService;

    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
            //************ 검증 로직 필요 **********************//
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
            log.info("CONNECT message = {}",message);
            String destination = ((String) message.getHeaders().get("simpDestination")).substring(7);
            Long partyId = Long.valueOf(destination).longValue();
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            chatSessionService.createBySessionIdAndPartyId(sessionId, partyId);
            partyService.updateActiveNumber(partyId, 1);
            log.info("SUBSCRIBED RoomId{}", partyId);

        } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료
            log.info("DISCONNECT message = {}",message);
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            Long partyId =  chatSessionService.deleteBySessionId(sessionId);
            partyService.updateActiveNumber(partyId, -1);
            log.info("DISCONNECT RoomId{}", partyId);
        }
        return message;
    }

}
