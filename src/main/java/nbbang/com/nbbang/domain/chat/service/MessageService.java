package nbbang.com.nbbang.domain.chat.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.MessageType;
import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.domain.party.service.PartyService;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.webjars.NotFoundException;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

import static nbbang.com.nbbang.domain.chat.controller.ChatResponseMessage.MESSAGE_NOT_FOUND;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final PartyService partyService;
    private final MemberService memberService;

    @Transactional
    public Long send(Long partyId, Long senderId, String content) {
        Party party = partyService.findById(partyId);
        Member sender = memberService.findById(senderId);
        Long orderInChat = countByPartyId(partyId);
        Integer readNumber = party.getActiveNumber();
        Message message = Message.builder().readNumber(0).content(content).type(MessageType.CHAT).readNumber(readNumber)
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
