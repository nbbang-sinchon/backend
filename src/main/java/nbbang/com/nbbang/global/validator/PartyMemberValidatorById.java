package nbbang.com.nbbang.global.validator;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Component
public class PartyMemberValidatorById {

    private final PartyService partyService;
    private final MemberService memberService;
    private final PartyMemberValidator partyMemberValidator;

    @Transactional
    public void isPartyMember(Long partyId, Long memberId){
        partyMemberValidator.isPartyMember(partyService.findById(partyId),memberService.findById(memberId));
    }
}
