package nbbang.com.nbbang.global.validator;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartyMemberValidatorService {
    private final PartyService partyService;
    private final MemberService memberService;

    public PartyMemberValidatorDto createById(Long partyId, Long memberId){
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);
        PartyMemberValidatorDto dto = PartyMemberValidatorDto.builder().party(party).member(member).build();
        return dto;
    }
    public PartyMemberValidatorDto createByUriAndMemberId(String requestUri, Long memberId){
        Long partyId = convertUriToPartyId(requestUri);
        return createById(partyId, memberId);
    }

    private Long convertUriToPartyId(String requestUri) {
        String[] split = requestUri.split("/");
        return Long.valueOf(split[2]);
    }
}
