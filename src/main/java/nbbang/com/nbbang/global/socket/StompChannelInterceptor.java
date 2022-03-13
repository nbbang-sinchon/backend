
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
import nbbang.com.nbbang.global.validator.PartyMemberValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.List;

// https://daddyprogrammer.org/post/5290/spring-websocket-chatting-server-enter-qut-event-view-user-count/
// https://stackoverflow.com/questions/44852776/spring-mvc-websockets-with-stomp-authenticate-against-specific-channels
@Slf4j
@Component
//@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StompChannelInterceptor implements ChannelInterceptor {

    private final ChatService chatService;
    private final PartyService partyService;
    private final MemberService memberService;
    private final PartyMemberRepository partyMemberRepository;
    private final PartyMemberService partyMemberService;
    private final SessionPartyGlobalRepository sessionPartyGlobalRepository;
    private final SocketSender socketSender;
    private final CurrentMember currentMember;
    private final PartyMemberValidator partyMemberValidator;


    public StompChannelInterceptor(ChatService chatService, PartyService partyService, MemberService memberService,
                                   PartyMemberRepository partyMemberRepository, PartyMemberService partyMemberService,
                                   SessionPartyGlobalRepository sessionPartyGlobalRepository,
                                   @Lazy SocketSender socketSender, CurrentMember currentMember,
                                   PartyMemberValidator partyMemberValidator) {
        this.chatService = chatService;
        this.partyService = partyService;
        this.memberService = memberService;
        this.partyMemberRepository = partyMemberRepository;
        this.partyMemberService = partyMemberService;
        this.socketSender = socketSender;
        this.sessionPartyGlobalRepository = sessionPartyGlobalRepository;
        this.currentMember = currentMember;
        this.partyMemberValidator = partyMemberValidator;
    }

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
        String destination = accessor.getDestination();
        attributes.put("memberId", memberId(message));
        Long memberId = (Long) attributes.get("memberId");

        if (StompCommand.CONNECT == accessor.getCommand()) {
            connect(attributes);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            if(destination.startsWith(TOPIC_GLOBAL))  {
                Long globalMemberId = Long.valueOf(destination.substring(14));
                if(memberId!=globalMemberId){throw new RuntimeException("자신의 소켓만 구독할 수 있습니다. ");}
            }
            else if(destination.startsWith(TOPIC_CHATTING)){
                Long partyId = Long.valueOf(destination.substring(16));
                partyMemberValidator.isPartyMember(partyService.findById(partyId),memberService.findById(memberId));
                enterChatRoom(attributes, partyId);
            }else if(destination.startsWith(TOPIC_BREAD_BOARD)){
                Long partyId = Long.valueOf(destination.substring(18));
                partyMemberValidator.isPartyMember(partyService.findById(partyId),memberService.findById(memberId));
            }
            else{
                throw new IllegalArgumentException("올바른 토픽을 입력해주세요.");
            }
        } else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) {
            if(destination.startsWith(TOPIC_CHATTING)){
                Long partyId = Long.valueOf(destination.substring(18));
                exitChatRoom(attributes, partyId);
            }
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            exitChatRoomIfExist(attributes);
        }
        return message;
    }

    public void connect(Map<String, Object> attributes) {
        attributes.put("status", "none");
    }

    public void enterChatRoom(Map<String, Object> attributes, Long partyId){
        attributes.put("partyId", partyId);
        attributes.put("status", "subscribe");
        Long memberId = (Long) attributes.get("memberId");
        readMessage(partyId, memberId);
        sessionPartyGlobalRepository.subscribe(partyId, memberId);
    }

    @Transactional
    public void readMessage(Long partyId, Long memberId) {
        Long lastReadMessageId = chatService.readMessage(partyId, memberId);
        ChatReadSocketDto chatReadSocketDto = ChatReadSocketDto.builder().lastReadMessageId(lastReadMessageId).build();
        socketSender.sendChattingReadMessage(partyId, chatReadSocketDto);
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
