package nbbang.com.nbbang.domain.chat.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.global.exception.MessageNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {
    private final MessageRepository messageRepository;
    private final MemberService memberService;
    private final PartyRepository partyRepository;

    public Long findLastMessageId(Long partyId) {
        return messageRepository.findLastMessageId(partyId);
    }

    public Message findById(Long messageId) {
        return messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);
    }

    public Page<Message> findMessages(Long partyId, Pageable pageable) {
        return messageRepository.findAllByPartyId(partyId, pageable);
    }

    public Page<Message> findMessages(Long partyId, Pageable pageable, Long startId) {
        return messageRepository.findAllByPartyId(partyId, pageable);
    }

    @Transactional
    public Long sendMessage(Long memberId, Long partyId, String content, LocalDateTime localDateTime) {
        Member member = memberService.findById(memberId);
        Party party = partyRepository.getById(partyId);
        Message message = Message.createMessage(member, party, content, localDateTime);
        messageRepository.save(message);
        return message.getId();
    }

}
