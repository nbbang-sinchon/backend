package nbbang.com.nbbang.global.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.global.error.exception.NotOwnerException;
import nbbang.com.nbbang.global.error.exception.NotPartyMemberException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PartyMemberValidator {

    public boolean validatePartyMember(PartyMemberValidatorDto dto) {
        return validatePartyMember(dto.getParty(), dto.getMember());
    }

    public boolean validateOwner(PartyMemberValidatorDto dto) {
        return validateOwner(dto.getParty(), dto.getMember());
    }

    public boolean validatePartyMember(Party party, Member member) {
        if(!party.getPartyMembers().stream().anyMatch(mp -> mp.getMember().equals(member))){
            throw new NotPartyMemberException();
        }
        return true;
    }
    public boolean validateOwner(Party party, Member member) {
        if(!(Optional.ofNullable(party.getOwner()).orElse(Member.builder().build()).equals(member))){
             throw new NotOwnerException();
        }
        return true;
    }

}
