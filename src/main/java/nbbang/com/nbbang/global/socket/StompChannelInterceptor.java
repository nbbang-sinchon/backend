
package nbbang.com.nbbang.global.socket;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;
import nbbang.com.nbbang.domain.bbangpan.repository.PartyMemberRepository;
import nbbang.com.nbbang.domain.chat.dto.ChatReadSocketDto;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.repository.SessionPartyGlobalRepository;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.security.jwt.JwtService;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

// https://daddyprogrammer.org/post/5290/spring-websocket-chatting-server-enter-qut-event-view-user-count/
// https://stackoverflow.com/questions/44852776/spring-mvc-websockets-with-stomp-authenticate-against-specific-channels
@Slf4j
@Component
//@RequiredArgsConstructor
public class StompChannelInterceptor implements ChannelInterceptor {

    private final ChatService chatService;
    private final PartyService partyService;
    private final MemberService memberService;
    private final PartyMemberRepository partyMemberRepository;
    private final PartyMemberService partyMemberService;
    private final SessionPartyGlobalRepository sessionPartyGlobalRepository;
    private final JwtService jwtService;
    private final SocketSender socketSender;
    private final CurrentMember currentMember;


    public StompChannelInterceptor(ChatService chatService, PartyService partyService, MemberService memberService,
                                   PartyMemberRepository partyMemberRepository, PartyMemberService partyMemberService,
                                   SessionPartyGlobalRepository sessionPartyGlobalRepository,
                                   JwtService jwtService, @Lazy SocketSender socketSender, CurrentMember currentMember) {
        this.chatService = chatService;
        this.partyService = partyService;
        this.memberService = memberService;
        this.partyMemberRepository = partyMemberRepository;
        this.partyMemberService = partyMemberService;
        this.jwtService = jwtService;
        this.socketSender = socketSender;
        this.sessionPartyGlobalRepository = sessionPartyGlobalRepository;
        this.currentMember = currentMember;
    }

    private static final String TOPIC_GLOBAL = "/topic/global";
    private static final String TOPIC_CHATTING = "/topic/chatting";
    private static final String TOPIC_BREAD_BOARD = "/topic/breadBoard";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Map<String, Object> attributes = accessor.getSessionAttributes();
        String destination = accessor.getDestination();
        if (StompCommand.CONNECT == accessor.getCommand()) {
            Long memberId = 1L; // current member로 수정
            connect(attributes, memberId);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            if(destination.startsWith(TOPIC_GLOBAL))  {
                Long globalMemberId = Long.valueOf(destination.substring(14));
                // 스스로가 아니면 거절하는 로직 추가 //
                log.info("SUBSCRIBED memberId: {}", globalMemberId);
            }
            else if(destination.startsWith(TOPIC_CHATTING)){
                Long partyId = Long.valueOf(destination.substring(16));
                // 파티원이 아니면 거절하는 로직 추가 //
                enterChatRoom(attributes, partyId);
                log.info("SUBSCRIBED chat RoomId: {}", partyId);
            }else if(destination.startsWith(TOPIC_BREAD_BOARD)){
                Long partyId = Long.valueOf(destination.substring(18));
                // 파티원이 아니면 거절하는 로직 추가 //
                log.info("SUBSCRIBED breadBoard RoomId: {}", partyId);
            }
            else{
                throw new IllegalArgumentException("올바른 토픽을 입력해주세요.");
            }
        } else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) {
            if(destination.startsWith(TOPIC_CHATTING)){
                Long partyId = Long.valueOf(destination.substring(18));
                exitChatRoom(attributes, partyId);
                log.info("UNSUBSCRIBE RoomId: {}", partyId);
            }
            else{
                log.info("UNSUBSCRIBE.");
            }
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            exitChatRoomIfExist(attributes);
        }
        return message;
    }

    public void connect(Map<String, Object> attributes, Long memberId) {
        attributes.put("status", "none");
        attributes.put("memberId",memberId);
    }

    public void enterChatRoom(Map<String, Object> attributes, Long partyId){
        attributes.put("partyId", partyId);
        attributes.put("status", "subscribe");
        Long memberId = (Long) attributes.get("memberId");
        readMessage(partyId, memberId);
        sessionPartyGlobalRepository.subscribe(partyId, memberId);
    }

    public void readMessage(Long partyId, Long memberId) {
        Long lastReadMessageId = chatService.readMessage(partyId, memberId);
        ChatReadSocketDto chatReadSocketDto = ChatReadSocketDto.builder().lastReadMessageId(lastReadMessageId).build();
        socketSender.sendChatting(partyId, chatReadSocketDto);
    }

    public void exitChatRoom(Map<String, Object> attributes, Long partyId) {
        Long memberId = (Long) attributes.get("memberId");
        sessionPartyGlobalRepository.unsubscribe(partyId, memberId);
        attributes.put("status", "unsubscribe");
        PartyMember partyMember = partyMemberRepository.findByMemberIdAndPartyId(memberId, partyId);
        nbbang.com.nbbang.domain.chat.domain.Message currentLastMessage = partyService.findLastMessage(partyId);
        partyMemberService.updateLastReadMessage(partyMember, currentLastMessage);
    }

    public void exitChatRoomIfExist(Map<String, Object> attributes) {
        if(attributes.get("status").equals("subscribe")){
            Long partyId = (Long) attributes.get("partyId");
            exitChatRoom(attributes, partyId);
        }
    }
}
