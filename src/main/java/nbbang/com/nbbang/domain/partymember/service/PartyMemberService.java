package nbbang.com.nbbang.domain.partymember.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.domain.partymember.domain.PartyMember;
import nbbang.com.nbbang.domain.partymember.repository.PartyMemberRepository;
import nbbang.com.nbbang.global.cache.PartyMemberCacheService;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.MessageType;
import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import nbbang.com.nbbang.domain.chat.service.MessageService;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.controller.PartyResponseMessage;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.exception.PartyExitForbiddenException;
import nbbang.com.nbbang.domain.party.exception.PartyJoinException;
import nbbang.com.nbbang.global.error.exception.NotPartyMemberException;
import nbbang.com.nbbang.global.socket.service.SocketPartyMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
@Slf4j
public class PartyMemberService {
    private final PartyMemberRepository partyMemberRepository;
    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private final SocketPartyMemberService socketPartyMemberService;
    private final PartyMemberCacheService partyMemberCacheService;

    public boolean isPartyMember(Party party, Member member) {
        return Optional.ofNullable(party.getOwner()).equals(member) || party.getPartyMembers().stream().anyMatch(mp -> mp.getMember().equals(member));
    }

    // refactored
    public boolean isPartyOwnerOrMember(Long partyId, Long memberId) {
        return partyMemberRepository.findByPartyIdAndMemberId(partyId, memberId) != null;
    }

    @Transactional
    public PartyMember joinParty(Party party, Member member) {
        // 이미 참여한 파티일 경우
        if (isPartyMember(party, member)) {
            throw new PartyJoinException(PartyResponseMessage.PARTY_DUPLICATE_JOIN_ERROR);
        }
        // 파티가 찼을 경우
        if (party.getGoalNumber().equals(party.getPartyMembers().size())) {
            throw new PartyJoinException(PartyResponseMessage.PARTY_FULL_ERROR);
        }
        // 파티 STATUS 가 full 또는 closed 일 경우
        if (party.getStatus().equals(PartyStatus.FULL) || party.getStatus().equals(PartyStatus.CLOSED)) {
            throw new PartyJoinException(PartyResponseMessage.PARTY_JOIN_NONOPEN_ERROR);
        }

        PartyMember partyMember = PartyMember.createPartyMember(party, member);
        partyMemberCacheService.evictPartyMemberCache(party.getId());

        //Message enterMessage = messageService.send(party.getId(), member.getId(), member.getNickname() + " 님이 입장하셨습니다.", MessageType.ENTER);
        Message enterMessage = messageService.send(party, member.getId(), member.getNickname() + " 님이 입장하셨습니다.", MessageType.ENTER);

        partyMember.changeLastReadMessage(enterMessage);
        partyMemberRepository.save(partyMember);
        return partyMember;
    }


    @Transactional
    public void exitParty(Party party, Member member) {
        // 참여하지 않은 파티일 경우
        if (!isPartyMember(party, member)) {
            throw new NotPartyMemberException();
        }
        // 방장이 탈퇴하려 했을 경우
        if (party.getOwner().equals(member)) {
            throw new PartyExitForbiddenException(PartyResponseMessage.PARTY_OWNER_EXIT_ERROR);
        }

        PartyMember partyMember = partyMemberRepository.findByPartyIdAndMemberId(party.getId(), member.getId());
        party.exitPartyMember(partyMember);
        partyMemberRepository.delete(partyMember);

        partyMemberCacheService.evictPartyMemberCache(party.getId());
        messageService.send(party.getId(), member.getId(), member.getNickname() + " 님이 퇴장하셨습니다.", MessageType.EXIT);

    }


    @Transactional
    public void changeField(Long partyId, Long memberId, Field field, Object value) throws NoSuchFieldException {
        PartyMember partyMember = partyMemberRepository.findByPartyIdAndMemberId(partyId, memberId);
        if (field.equals(PartyMember.getField("price"))){
            partyMember.changePrice((Integer) value);
        }else if(field.equals(PartyMember.getField("isSent"))){
            partyMember.changeIsSent((Boolean) value);
        }else{
            log.info("no such field");
        }
    }

    public Message findLastReadMessage(Long partyId, Long memberId) {
        PartyMember partyMember = partyMemberRepository.findByPartyIdAndMemberId(partyId, memberId);
        return Optional.ofNullable(partyMember.getLastReadMessage()).orElse(Message.builder().id(0L).build());
    }


    public List getNotReadNumbers(List<Party> parties, Long memberId) {
        List<Integer> notReadNumbers = new ArrayList<>();
        for (Party party : parties) {
            if(socketPartyMemberService.isActive(party.getId(), memberId)){
                notReadNumbers.add(0);
            }
            else{
                notReadNumbers.add(messageRepository.countByPartyIdAndIdGreaterThan(party.getId(), findLastReadMessage(party.getId(), memberId).getId()));
            }
        }
        return notReadNumbers;
    }

    public Message getEnterMessage(Long partyId, Long memberId) {
        Message message = Optional.ofNullable(messageRepository.findFirstByTypeAndPartyIdAndSenderId(MessageType.ENTER, partyId, memberId))
                .orElse(Message.builder().id(0L).build());
        return message;
    }

}
