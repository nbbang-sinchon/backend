package nbbang.com.nbbang.global.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.error.exception.NotOwnerException;
import nbbang.com.nbbang.global.error.exception.NotPartyMemberException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PartyMemberValidator {

    public boolean isPartyMember(PartyMemberValidatorDto dto) {
        return isPartyMember(dto.getParty(), dto.getMember());
    }

    public boolean isOwner(PartyMemberValidatorDto dto) {
        log.info("dto in validator: {}", dto);
        return isOwner(dto.getParty(), dto.getMember());
    }

    public boolean isPartyMember(Party party, Member member) {
        if(!party.getPartyMembers().stream().anyMatch(mp -> mp.getMember().equals(member))){
            throw new NotPartyMemberException();
        }
        return true;
    }
    public boolean isOwner(Party party, Member member) {
        log.info("party: {}", party);
        log.info("member: {}", member);
        if(!(Optional.ofNullable(party.getOwner()).orElse(Member.builder().build()).equals(member))){
             throw new NotOwnerException();
        }
        return true;
    }

}
