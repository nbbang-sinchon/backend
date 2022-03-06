
package nbbang.com.nbbang.global.socket;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.dto.ChatReadSocketDto;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.chat.service.ChatSessionService;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.domain.party.service.SessionPartyService;
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
    private final MemberService memberService;
    private final SessionMemberService sessionMemberService;
    private final SessionPartyService sessionPartyService;

    public StompChannelInterceptor(ChatService chatService, PartyService partyService, ChatSessionService chatSessionService, MemberService memberService,
                                   SessionMemberService sessionMemberService, SessionPartyService sessionPartyService,
                                   PartyMemberService partyMemberService, JwtService jwtService, @Lazy SocketSender socketSender) {
        this.chatService = chatService;
        this.partyService = partyService;
        this.chatSessionService = chatSessionService;
        this.memberService = memberService;
        this.partyMemberService = partyMemberService;
        this.jwtService = jwtService;
        this.socketSender = socketSender;
        this.sessionMemberService = sessionMemberService;
        this.sessionPartyService = sessionPartyService;
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
        String destination = accessor.getDestination();
        String session = accessor.getSessionId();
        if (StompCommand.CONNECT == accessor.getCommand()) {
            log.info("CONNECT message = {}",message);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            log.info("SUBSCRIBED message = {}",message);
            if(destination.startsWith(TOPIC_GLOBAL))  {
                Long memberId = Long.valueOf(destination.substring(14));
                log.info("SUBSCRIBED memberId: {}", memberId);
            }
            else if(destination.startsWith(TOPIC_CHATTING)){
                Long partyId = Long.valueOf(destination.substring(16));
                enterChatRoom(session, partyId);
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
            if(destination.startsWith(TOPIC_CHATTING)){
                Long partyId = Long.valueOf(destination.substring(18));
                exitChatRoom(session, partyId);
                log.info("UNSUBSCRIBE RoomId: {}", partyId);
            }
            else{
                log.info("UNSUBSCRIBE.");
            }
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
             exitChatRoomIfExist(session);
             sessionMemberService.deleteSession(accessor.getSessionId());
        }
        return message;
    }

    private void enterChatRoom(String session, Long partyId){
        Long memberId = sessionMemberService.findMemberId(session);
        readMessage(partyId, memberId);
        sessionPartyService.addSession(partyId, session, memberId);
    }

    private void readMessage(Long partyId, Long memberId) {
        Long lastReadMessageId = chatService.readMessage(partyId, memberId);
        ChatReadSocketDto chatReadSocketDto = ChatReadSocketDto.builder().lastReadMessageId(lastReadMessageId).build();
        socketSender.sendChatting(partyId, chatReadSocketDto);
    }

    private void exitChatRoom(String session, Long partyId) {
        Long memberId = sessionMemberService.findMemberId(session);
        sessionPartyService.deleteSession(partyId, memberId);
    }

    private void exitChatRoomIfExist(String session) {
        Long memberId = sessionMemberService.findMemberId(session);
        if(memberService.findById(memberId).getActivePartyId()!=null){
            exitChatRoom(session, memberService.findById(memberId).getActivePartyId());
        }
    }
}
