package nbbang.com.nbbang.global.socket.service;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.partymember.repository.PartyMemberRepository;
import nbbang.com.nbbang.global.socket.dto.SocketReadMessageDto;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.global.socket.SocketSender;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@Slf4j
public class SocketChatRoomService {

    private final ChatService chatService;
    private final PartyMemberRepository partyMemberRepository;
    private final SocketPartyMemberService socketPartyMemberService;
    private final SocketSender socketSender;

    public SocketChatRoomService(ChatService chatService, PartyMemberRepository partyMemberRepository,
                                 SocketPartyMemberService socketPartyMemberService,
                                 @Lazy SocketSender socketSender) {
        this.chatService = chatService;
        this.partyMemberRepository = partyMemberRepository;
        this.socketSender = socketSender;
        this.socketPartyMemberService = socketPartyMemberService;
    }

    @Transactional
    public void enter(Long partyId, Long memberId){
        socketPartyMemberService.subscribe(partyId, memberId);
        if(socketPartyMemberService.getPartyMemberActiveNumber(partyId, memberId)==1){
            readMessage(partyId, memberId, true);
        }
    }

    @Transactional
    public void readMessage(Long partyId, Long memberId, Boolean isSocket) {
        if((isSocket||!socketPartyMemberService.isActive(partyId, memberId))){
            SocketReadMessageDto socketReadMessageDto = chatService.readMessage(partyId, memberId);
            socketSender.sendChattingReadMessage(partyId, socketReadMessageDto);
        }
    }

    @Transactional
    public void exit(Long partyId, Long memberId) {
        socketPartyMemberService.unsubscribe(partyId, memberId);
        partyMemberRepository.updateLastReadMessage(partyId, memberId);
    }

}
