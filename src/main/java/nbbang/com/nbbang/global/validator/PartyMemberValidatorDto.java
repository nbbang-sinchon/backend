package nbbang.com.nbbang.global.validator;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;

@Data
@Builder
public class PartyMemberValidatorDto {
    private Party party;
    private Member member;
}
