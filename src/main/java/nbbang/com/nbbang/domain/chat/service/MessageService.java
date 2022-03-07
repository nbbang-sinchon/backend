package nbbang.com.nbbang.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.MessageType;
import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.domain.party.repository.SessionPartyMemoryRepository;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.domain.party.service.SessionPartyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import static nbbang.com.nbbang.domain.chat.controller.ChatResponseMessage.MESSAGE_NOT_FOUND;
import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.PARTY_NOT_FOUND;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final PartyRepository partyRepository;
    private final MemberService memberService;
    private final SessionPartyService sessionPartyService;

    @Transactional
    public Long send(Long partyId, Long senderId, String content) {
        Party party = partyRepository.findById(partyId).orElseThrow(()->new NotFoundException(PARTY_NOT_FOUND));
        Member sender = memberService.findById(senderId);
        Long orderInChat = countByPartyId(partyId);
        Integer readNumber = sessionPartyService.getActiveNumber(partyId);
        Message message = Message.builder().readNumber(0).content(content).type(MessageType.CHAT).readNumber(readNumber)
                .party(party).sender(sender).build();
        Message savedMessage = messageRepository.save(message);
        return savedMessage.getId();
    }

    @Transactional
    public Long send(Long partyId, Long senderId, String content, MessageType type) {
        Party party = partyRepository.findById(partyId).orElseThrow(()->new NotFoundException(PARTY_NOT_FOUND));
        Member sender = memberService.findById(senderId);
        Long orderInChat = countByPartyId(partyId);
        Integer readNumber = sessionPartyService.getActiveNumber(partyId);
        Message message = Message.builder().readNumber(0).content(content).type(type).readNumber(readNumber)
                .party(party).sender(sender).build();
        Message savedMessage = messageRepository.save(message);
        return savedMessage.getId();
    }

    public Message findById(Long id){
        return messageRepository.findById(id).orElseThrow(()->new NotFoundException(MESSAGE_NOT_FOUND));
    }

    private Long countByPartyId(Long partyId){
        Long count = messageRepository.countByPartyId(partyId);
        return count;
    }

}
