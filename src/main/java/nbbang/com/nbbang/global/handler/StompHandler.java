
package nbbang.com.nbbang.global.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.chat.service.ChatSessionService;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.security.jwt.JwtService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
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
    private final JwtService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
            log.info("CONNECT message = {}",message);
            // String token = accessor.getFirstNativeHeader("token");
            // log.info("token = {}",token);
            // Long id = jwtService.validateByToken(token);
            // log.info("id = {}",id);
            // throw new IllegalArgumentException("test error");
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
            log.info("SUBSCRIBED message = {}",message);
            log.info("SUBSCRIBED destination = {}",accessor.getDestination());
/*            String destination = accessor.getDestination();
            if(destination.startsWith("/global"))  {
                Long memberId = Long.valueOf(destination.substring(8));
                log.info("SUBSCRIBED memberId: {}", memberId);
            }
            else if(destination.startsWith("/topic")){
                Long partyId = Long.valueOf(destination.substring(7));
                String sessionId = accessor.getSessionId();
                chatSessionService.createBySessionIdAndPartyId(sessionId, partyId);
                partyService.updateActiveNumber(partyId, 1);
                log.info("SUBSCRIBED RoomId: {}", partyId);
            }
            else{
                throw new IllegalArgumentException("올바른 토픽을 입력해주세요.");
            }*/
        } else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) { //
            log.info("UNSUBSCRIBE message = {}",message);
            // Long partyId = exitChatRoomIfExist(message);
            // log.info("UNSUBSCRIBE RoomId: {}", partyId);
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료 : DISCONNECT
            log.info("DISCONNECT message = {}",message);
            // Long partyId = exitChatRoomIfExist(message);
            // log.info("DISCONNECT RoomId: {}", partyId);
        }
        return message;
    }

    private Long exitChatRoomIfExist(Message<?> message) {
        String sessionId = (String) message.getHeaders().get("simpSessionId");
        Long partyId =  chatSessionService.deleteIfExistBySessionId(sessionId);
        if (partyId!=-1) {
            partyService.updateActiveNumber(partyId, -1);
            //************* memberId 넣는 로직 추가해야함 *************
            chatService.exitChatRoom(partyId, 1L);
        }
        return partyId;
    }
}
