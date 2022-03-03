package nbbang.com.nbbang.domain.bbangpan.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.party.domain.Party;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class BbangpanReadResponseDto {

    private String bank;
    private Integer account;
    private Integer deliveryFee;
    private List<PartyMemberResponseDto> partyMembers;

    public static BbangpanReadResponseDto createDtoByParty(Party party) {
        return BbangpanReadResponseDto.builder()
                .bank(party.getBank())
                .account(party.getAccount())
                .deliveryFee(party.getDeliveryFee())
                .partyMembers(party.getPartyMembers().stream().map(PartyMemberResponseDto::createDtoByEntity).collect(Collectors.toList()))
                .build();
    }

}
