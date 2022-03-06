
package nbbang.com.nbbang.global.socket;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.dto.ChatReadSocketDto;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.chat.service.ChatSessionService;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.domain.party.service.PartySessionService;
import nbbang.com.nbbang.global.security.jwt.JwtService;
import org.springframework.context.annotation.Lazy;
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
//@RequiredArgsConstructor
public class StompChannelInterceptor implements ChannelInterceptor {


    private final ChatService chatService;
    private final PartyService partyService;
    private final ChatSessionService chatSessionService;
    private final MemberSessionService memberSessionService;
    private final PartySessionService partySessionService;

    public StompChannelInterceptor(ChatService chatService, PartyService partyService, ChatSessionService chatSessionService,
                                   MemberSessionService memberSessionService,PartySessionService partySessionService,
                                   PartyMemberService partyMemberService, JwtService jwtService, @Lazy SocketSender socketSender) {
        this.chatService = chatService;
        this.partyService = partyService;
        this.chatSessionService = chatSessionService;
        this.partyMemberService = partyMemberService;
        this.jwtService = jwtService;
        this.socketSender = socketSender;
        this.memberSessionService = memberSessionService;
        this.partySessionService = partySessionService;
    }

    private final PartyMemberService partyMemberService;
    private final JwtService jwtService;
    private final SocketSender socketSender;

    private static final String TOPIC_GLOBAL = "/topic/global";
    private static final String TOPIC_CHATTING = "/topic/chatting";
    private static final String TOPIC_BREAD_BOARD = "/topic/breadBoard";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            log.info("CONNECT message = {}",message);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            log.info("SUBSCRIBED message = {}",message);
            String destination = accessor.getDestination();
            if(destination.startsWith(TOPIC_GLOBAL))  {
                Long memberId = Long.valueOf(destination.substring(14));
                log.info("SUBSCRIBED memberId: {}", memberId);
            }
            else if(destination.startsWith(TOPIC_CHATTING)){
                Long partyId = Long.valueOf(destination.substring(16));
                enterChatRoom(accessor, partyId);
                log.info("SUBSCRIBED chat RoomId: {}", partyId);
            }else if(destination.startsWith(TOPIC_BREAD_BOARD)){
                Long partyId = Long.valueOf(destination.substring(18));
                log.info("SUBSCRIBED breadBoard RoomId: {}", partyId);
            }
            else{
                throw new IllegalArgumentException("올바른 토픽을 입력해주세요.");
            }
        } else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) {
            log.info("UNSUBSCRIBE message = {}",message);
             Long partyId = exitChatRoomIfExist(accessor);
             log.info("UNSUBSCRIBE RoomId: {}", partyId);
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            log.info("DISCONNECT message = {}",message);
             Long partyId = exitChatRoomIfExist(accessor);
             log.info("DISCONNECT RoomId: {}", partyId);
        }
        return message;
    }

    private void enterChatRoom(StompHeaderAccessor accessor, Long partyId){
        String session = accessor.getSessionId();
        Long memberId = memberSessionService.findMemberId(session);
        readMessage(partyId, memberId);
        partySessionService.addSession(partyId, memberId, session);
        readMessage(partyId, 1L);
    }

    private void readMessage(Long partyId, Long memberId) {
        Long lastReadMessageId = chatService.readMessage(partyId, memberId);
        ChatReadSocketDto chatReadSocketDto = ChatReadSocketDto.builder().lastReadMessageId(lastReadMessageId).build();
        socketSender.sendChatting(partyId, chatReadSocketDto);
    }

    private Long exitChatRoomIfExist(StompHeaderAccessor accessor) {
        String session = accessor.getSessionId();
        memberSessionService.findMemberId(session);
        Long partyId =  1L; //chatSessionService.deleteIfExistBySessionId(sessionId);
        if (partyId!=-1) {
            //partyService.updateActiveNumber(partyId, -1);
            //************* memberId 넣는 로직 추가해야함 *************
            chatService.exitChatRoom(partyId, 1L);
        }
        return partyId;
    }
}
