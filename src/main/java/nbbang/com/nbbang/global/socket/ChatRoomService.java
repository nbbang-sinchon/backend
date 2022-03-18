package nbbang.com.nbbang.global.socket;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;
import nbbang.com.nbbang.domain.bbangpan.repository.PartyMemberRepository;
import nbbang.com.nbbang.domain.chat.dto.ChatReadSocketDto;
import nbbang.com.nbbang.domain.chat.dto.ReadMessageDto;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.socket.service.SocketPartyMemberService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
@Slf4j
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

    @Transactional
    public void enter(Map<String, Object> attributes, Long partyId){
        attributes.put("partyId", partyId);
        Long memberId = (Long) attributes.get("memberId");
        socketPartyMemberService.subscribe(partyId, memberId);
        attributes.put("status", "subscribe");
        if(socketPartyMemberService.getPartyMemberActiveNumber(partyId, memberId)==1){
            readMessage(partyId, memberId, true);
        }
    }

    @Transactional
    public void readMessage(Long partyId, Long memberId, Boolean isSocket) {
        if((!socketPartyMemberService.isActive(partyId, memberId))||isSocket){
            ReadMessageDto readMessageDto = chatService.readMessage(partyId, memberId);
            socketSender.sendChattingReadMessage(partyId, readMessageDto);
        }
    }

    @Transactional
    public void exit(Map<String, Object> attributes, Long partyId) {
        Long memberId = (Long) attributes.get("memberId");
        socketPartyMemberService.unsubscribe(partyId, memberId);
        attributes.put("status", "unsubscribe");
        PartyMember partyMember = partyMemberRepository.findByMemberIdAndPartyId(memberId, partyId);
        Optional.ofNullable(partyMember).ifPresent(pm->partyMemberService.updateLastReadMessage(pm, partyService.findLastMessage(partyId)));
    }

    @Transactional
    public void exitIfSubscribing(Map<String, Object> attributes) {
        if(attributes.get("status").equals("subscribe")){
            Long partyId = (Long) attributes.get("partyId");
            exit(attributes, partyId);
        }
    }
}
