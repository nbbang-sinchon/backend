
package nbbang.com.nbbang.global.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import javax.print.DocFlavor;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// https://daddyprogrammer.org/post/5290/spring-websocket-chatting-server-enter-qut-event-view-user-count/
// https://stackoverflow.com/questions/44852776/spring-mvc-websockets-with-stomp-authenticate-against-specific-channels
@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    // private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;
    private final PartyService partyService;

    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
            // String jwtToken = accessor.getFirstNativeHeader("token");
            // log.info("CONNECT {}", jwtToken);
            // Header의 jwt token 검증
            // jwtTokenProvider.validateToken(jwtToken);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.

            System.out.println("CONNECT message = " + message);
            Map<String, List<String>> nativeHeaders = (Map) message.getHeaders().get("nativeHeaders");
            String destination = ((nativeHeaders.get("destination")).get(0)).substring(7);
            Long roomId = Long.valueOf(destination).longValue();
            partyService.updateActiveNumber(roomId, 1);
            log.info("SUBSCRIBED RoomId{}", roomId);
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료
/*            System.out.println("DISCONNECT message = " + message);
            Map<String, List<String>> nativeHeaders = (Map) message.getHeaders().get("nativeHeaders");
            String destination = ((nativeHeaders.get("destination")).get(0)).substring(7);
            Long roomId = Long.valueOf(destination).longValue();
            partyService.updateActiveNumber(roomId, -1);
            log.info("DISCONNECTED RoomId {}", roomId);*/
        }
        return message;
    }
}
