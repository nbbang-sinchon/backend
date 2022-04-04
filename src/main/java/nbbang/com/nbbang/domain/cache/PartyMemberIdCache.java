package nbbang.com.nbbang.domain.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartyMemberIdCache {

    private Long memberId;

    public static PartyMemberIdCache createByPartyMember(PartyMember pm) {
        return PartyMemberIdCache.builder().memberId(pm.getMember().getId()).build();
    }
}
