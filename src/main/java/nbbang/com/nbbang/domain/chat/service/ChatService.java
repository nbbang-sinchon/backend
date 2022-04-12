package nbbang.com.nbbang.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.partymember.domain.PartyMember;
import nbbang.com.nbbang.domain.partymember.repository.PartyMemberRepository;
import nbbang.com.nbbang.domain.chat.controller.ChatResponseMessage;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.MessageType;
import nbbang.com.nbbang.domain.chat.dto.ReadMessageDto;
import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.global.validator.PartyMemberValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final MessageRepository messageRepository;
    private final PartyMemberRepository partyMemberRepository;
    private final PartyMemberValidator partyMemberValidator;

    public Message findLastMessage(Party party) {
        return messageRepository.findLastMessage(party.getId());
    }

    public Message findById(Long messageId) {
        return messageRepository.findById(messageId).orElseThrow(() -> new NotFoundException(ChatResponseMessage.MESSAGE_NOT_FOUND));
    }

    public Message getEnterMessage(Long partyId, Long memberId) {
        Message message = Optional.ofNullable(messageRepository.findFirstByTypeAndPartyIdAndSenderId(MessageType.ENTER, partyId, memberId))
                .orElse(Message.builder().id(0L).build());
        return message;
    }

    public Page<Message> findMessages(Party party, Long memberId, Pageable pageable) {
        partyMemberValidator.validatePartyMember(party.getId(), memberId);
        Long enterMessageId = getEnterMessage(party.getId(), memberId).getId();
        return messageRepository.findAllByPartyIdAndIdGreaterThanEqualOrderByIdDesc(party.getId(),enterMessageId, pageable);
    }

    public Page<Message> findMessagesByCursorId(Party party, Long memberId, Pageable pageable, Long cursorId) {
        partyMemberValidator.validatePartyMember(party.getId(), memberId);
        Long enterMessageId = getEnterMessage(party.getId(), memberId).getId();
        return messageRepository.findAllByCursorId(party.getId(), enterMessageId, pageable, cursorId);
    }

    @Transactional
    public ReadMessageDto readMessage(Long partyId, Long memberId) {

        messageRepository.bulkNotReadSubtract(partyId, memberId);

        PartyMember partyMember = partyMemberRepository.findByPartyIdAndMemberId(partyId, memberId);
        Long lastReadMessageId = partyMember.getLastReadMessage().getId();
        partyMemberRepository.updateLastReadMessage(partyId, memberId);

        ReadMessageDto dto = ReadMessageDto.builder().lastReadMessageId(lastReadMessageId).senderId(memberId).build();
        return dto;
    }
}
