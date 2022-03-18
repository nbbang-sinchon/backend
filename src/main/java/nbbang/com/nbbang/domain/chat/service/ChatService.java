package nbbang.com.nbbang.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;
import nbbang.com.nbbang.domain.bbangpan.repository.PartyMemberRepository;
import nbbang.com.nbbang.domain.chat.controller.ChatResponseMessage;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.dto.ReadMessageDto;
import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.error.exception.NotPartyMemberException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final MessageRepository messageRepository;
    private final MemberService memberService;
    private final PartyRepository partyRepository;
    private final PartyService partyService;
    private final PartyMemberRepository partyMemberRepository;
    private final PartyMemberService partyMemberService;

    public Message findLastMessage(Party party) {
        return messageRepository.findLastMessage(party.getId());
    }

    public Message findById(Long messageId) {
        return messageRepository.findById(messageId).orElseThrow(() -> new NotFoundException(ChatResponseMessage.MESSAGE_NOT_FOUND));
    }

    public Page<Message> findMessages(Party party, Long memberId, Pageable pageable) {
        Long enterMessageId = partyMemberService.getEnterMessage(party.getId(), memberId).getId();
        return messageRepository.findAllByPartyIdAndIdGreaterThanEqualOrderByIdDesc(party.getId(),enterMessageId, pageable);
    }

    public Page<Message> findMessagesByCursorId(Party party, Long memberId, Pageable pageable, Long cursorId) {
        Long enterMessageId = partyMemberService.getEnterMessage(party.getId(), memberId).getId();
        return messageRepository.findAllByCursorId(party.getId(), enterMessageId, pageable, cursorId);
    }

    @Transactional
    public Long sendMessage(Long memberId, Long partyId, String content, LocalDateTime localDateTime) {
        Member member = memberService.findById(memberId);
        Party party = partyRepository.getById(partyId);

        boolean isMember = true;
        if (party.getOwner().getId() == memberId) {
            isMember = true;
        }
        if (party.getPartyMembers() != null) {
            for (PartyMember mp : party.getPartyMembers()) {
                if (mp.getMember().getId() == memberId) {
                    isMember = true;
                }
            }
        }
        if (!isMember) {
            throw new NotPartyMemberException();
        }

        Message message = Message.createMessage(member, party, content, localDateTime);
        messageRepository.save(message);
        return message.getId();
    }

    @Transactional
    public ReadMessageDto readMessage(Long partyId, Long memberId) {

        PartyMember partyMember = partyMemberRepository.findByMemberIdAndPartyId(memberId, partyId);

        Long lastReadMessageId = ((Optional.ofNullable(partyMember.getLastReadMessage())).orElse(Message.builder().id(-1L).build())).getId();
        messageRepository.bulkNotReadMinusPlus(lastReadMessageId, partyId);

        PartyMember reFoundPartyMember = partyMemberRepository.findByMemberIdAndPartyId(memberId, partyId);
        Message currentLastMessage = partyService.findLastMessage(partyId);
        reFoundPartyMember.changeLastReadMessage(currentLastMessage);
        ReadMessageDto dto = ReadMessageDto.builder().lastReadMessageId(lastReadMessageId).senderId(memberId).build();
        return dto;
    }

}
