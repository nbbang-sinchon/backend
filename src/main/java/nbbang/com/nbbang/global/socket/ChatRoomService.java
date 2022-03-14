package nbbang.com.nbbang.global.socket;

import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;
import nbbang.com.nbbang.domain.bbangpan.repository.PartyMemberRepository;
import nbbang.com.nbbang.domain.chat.dto.ChatReadSocketDto;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.socket.service.SocketPartyMemberService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional(readOnly = true)
@Service
public class ChatRoomService {

    private final ChatService chatService;
    private final PartyService partyService;
    private final PartyMemberRepository partyMemberRepository;
    private final PartyMemberService partyMemberService;
    private final SocketPartyMemberService socketPartyMemberService;
    private final SocketSender socketSender;

    public ChatRoomService(ChatService chatService, PartyService partyService,
                                   PartyMemberRepository partyMemberRepository, PartyMemberService partyMemberService,
                                   SocketPartyMemberService socketPartyMemberService,
                                   @Lazy SocketSender socketSender) {
        this.chatService = chatService;
        this.partyService = partyService;
        this.partyMemberRepository = partyMemberRepository;
        this.partyMemberService = partyMemberService;
        this.socketSender = socketSender;
        this.socketPartyMemberService = socketPartyMemberService;
    }

    public void enter(Map<String, Object> attributes, Long partyId){
        attributes.put("partyId", partyId);
        Long memberId = (Long) attributes.get("memberId");
        readMessage(partyId, memberId);
        socketPartyMemberService.subscribe(partyId, memberId);
        attributes.put("status", "subscribe");
    }

    @Transactional
    public void readMessage(Long partyId, Long memberId) {
        Long lastReadMessageId = chatService.readMessage(partyId, memberId);
        ChatReadSocketDto chatReadSocketDto = ChatReadSocketDto.builder().lastReadMessageId(lastReadMessageId).build();
        socketSender.sendChattingReadMessage(partyId, chatReadSocketDto);
    }

    public void exit(Map<String, Object> attributes, Long partyId) {
        Long memberId = (Long) attributes.get("memberId");
        socketPartyMemberService.unsubscribe(partyId, memberId);
        attributes.put("status", "unsubscribe");
        PartyMember partyMember = partyMemberRepository.findByMemberIdAndPartyId(memberId, partyId);
        nbbang.com.nbbang.domain.chat.domain.Message currentLastMessage = partyService.findLastMessage(partyId);
        partyMemberService.updateLastReadMessage(partyMember, currentLastMessage);
    }

    public void exitIfSubscribing(Map<String, Object> attributes) {
        if(attributes.get("status").equals("subscribe")){
            Long partyId = (Long) attributes.get("partyId");
            exit(attributes, partyId);
        }
    }
}
