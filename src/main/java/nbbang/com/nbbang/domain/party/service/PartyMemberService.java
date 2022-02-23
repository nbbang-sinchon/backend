package nbbang.com.nbbang.domain.party.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;
import nbbang.com.nbbang.domain.bbangpan.repository.PartyMemberRepository;
import nbbang.com.nbbang.domain.chat.domain.MessageType;
import nbbang.com.nbbang.domain.chat.service.MessageService;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.controller.PartyResponseMessage;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.exception.PartyExitForbiddenException;
import nbbang.com.nbbang.domain.party.exception.PartyJoinException;
import nbbang.com.nbbang.domain.party.repository.PartyHashtagRepository;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.global.error.exception.NotPartyMemberException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class PartyMemberService {
    private final PartyMemberRepository memberPartyRepository;
    private final MessageService messageService;

    public boolean isPartyOwnerOrMember(Party party, Member member) {
        return party.getOwner().equals(member) || party.getPartyMembers().stream().anyMatch(mp -> mp.getMember().equals(member));
    }

    @Transactional
    public Long joinParty(Party party, Member member) {
        // 이미 참여한 파티일 경우
        if (isPartyOwnerOrMember(party, member)) {
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
        // 이 부분 빵판 로직이 들어가야 할 거 같아서 나중에 bbangpan service 로 메소드를 만들어야 할 거 같습니다.
        PartyMember partyMember = PartyMember.createMemberParty(member, party);
        memberPartyRepository.save(partyMember);

        return messageService.send(party.getId(), member.getId(), member.getNickname() + " 님이 입장하셨습니다.", MessageType.ENTER);
    }


    @Transactional
    public Long exitParty(Party party, Member member) {
        // 참여하지 않은 파티일 경우
        if (!isPartyOwnerOrMember(party, member)) {
            throw new NotPartyMemberException();
        }
        // 방장이 탈퇴하려 했을 경우
        if (party.getOwner().equals(member)) {
            throw new PartyExitForbiddenException(PartyResponseMessage.PARTY_OWNER_EXIT_ERROR);
        }
        // 이 부분 빵판 로직이 들어가야 할 거 같아서 나중에 bbangpan service 로 메소드를 만들어야 할 거 같습니다.
        PartyMember partyMember = memberPartyRepository.findByMemberIdAndPartyId(member.getId(), party.getId());
        party.exitMemberParty(partyMember);
        memberPartyRepository.delete(partyMember);
        return messageService.send(party.getId(), member.getId(), member.getNickname() + " 님이 퇴장하셨습니다.", MessageType.EXIT);
    }
}
