package nbbang.com.nbbang.domain.chat.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;
import nbbang.com.nbbang.domain.chat.controller.ChatResponseMessage;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.global.error.exception.NotPartyMemberException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {
    // 최초 입장 시, 탈퇴시 메시지 보내는게 있어야함
    private final MessageRepository messageRepository;
    private final MemberService memberService;
    private final PartyRepository partyRepository;

    /*public Long findLastMessageId(Long partyId) {
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

    public Page<Message> findMessagesByCursorId(Long partyId, Pageable pageable, Long cursorId) {
        return messageRepository.findAllByCursorId(partyId, pageable, cursorId);
    }

    public void exitChat(Long partyId, Long memberId) {

    }

    public void changeStatus(Long partyId, Long memberId, PartyStatus status) {

    }

    public void changeGoalNumber(Long partyId, Long memberId, Integer goalNumber) {

    }

    @Transactional
    public Long sendMessage(Long memberId, Long partyId, String content, LocalDateTime localDateTime) {
        Member member = memberService.findById(memberId);
        Party party = partyRepository.getById(partyId);

        boolean isMember = true;
        if (party.getOwner().getId() == memberId) {
            isMember = true;
        }
        if (party.getMemberParties() != null) {
            for (MemberParty mp : party.getMemberParties()) {
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
    }*/

    public Message findLastMessage(Party party) {
        return messageRepository.findLastMessage(party.getId());
    }

    public Message findById(Long messageId) {
        return messageRepository.findById(messageId).orElseThrow(() -> new NotFoundException(ChatResponseMessage.MESSAGE_NOT_FOUND));
    }

    public Page<Message> findMessages(Party party, Pageable pageable) {
        return messageRepository.findAllByPartyId(party.getId(), pageable);
    }

    public Page<Message> findMessagesByCursorId(Party party, Pageable pageable, Long cursorId) {
        return messageRepository.findAllByCursorId(party.getId(), pageable, cursorId);
    }

    @Transactional
    public void exitChat(Party party, Member member) {

    }

    @Transactional
    public void changeStatus(Party party, Member member, PartyStatus status) {
        party.changeStatus(status);
    }

    @Transactional
    public void changeGoalNumber(Party party, Member member, Integer goalNumber) {
        party.changeGoalNumber(goalNumber);
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

}
