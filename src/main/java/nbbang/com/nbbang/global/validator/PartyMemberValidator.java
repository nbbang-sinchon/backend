package nbbang.com.nbbang.global.validator;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.error.exception.NotOwnerException;
import nbbang.com.nbbang.global.error.exception.NotPartyMemberException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PartyMemberValidator {
    private final PartyService partyService;
    private final MemberService memberService;

    public boolean isPartyMember(String requestUri, Long memberId) {
        Long partyId = convertUriToPartyId(requestUri);
        return isPartyMember(partyId, memberId);
    }

    public boolean isOwner(String requestUri, Long memberId) {
        Long partyId = convertUriToPartyId(requestUri);
        return isOwner(partyId, memberId);
    }

    public boolean isPartyMember(Long partyId, Long memberId) {
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);
        if(!party.getPartyMembers().stream().anyMatch(mp -> mp.getMember().equals(member))){
            throw new NotPartyMemberException();
        }
        return true;
    }
    public boolean isOwner(Long partyId, Long memberId) {
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);
        if(!Optional.ofNullable(party.getOwner()).equals(member)){
            throw new NotOwnerException();
        }
        return true;
    }

    private Long convertUriToPartyId(String requestUri) {
        String[] split = requestUri.split("/");
        return Long.valueOf(split[2]);
    }
}
