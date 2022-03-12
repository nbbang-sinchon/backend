package nbbang.com.nbbang.global.socket;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@Builder
public class PartyMemberPair implements Serializable {

    private Long partyId;
    private Long memberId;

    public static PartyMemberPair create(Long partyId, Long memberId){
        return PartyMemberPair.builder().partyId(partyId).memberId(memberId).build();
    }

}
