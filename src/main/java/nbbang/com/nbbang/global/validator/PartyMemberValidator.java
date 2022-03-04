package nbbang.com.nbbang.global.validator;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.error.exception.NotOwnerException;
import nbbang.com.nbbang.global.error.exception.NotPartyMemberException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PartyMemberValidator {
    private final PartyService partyService;
    private final MemberService memberService;

    public void validateOwner(Long partyId, Long memberId) {
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);
        if(Optional.ofNullable(party.getOwner()).equals(member)){}
        else{
            throw new NotOwnerException();
        }
    }
    public void validatePartyMember(Long partyId, Long memberId) {
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);
        if(party.getPartyMembers().stream().anyMatch(mp -> mp.getMember().equals(member))){}
        else{
            throw new NotPartyMemberException();
        }
    }
}
