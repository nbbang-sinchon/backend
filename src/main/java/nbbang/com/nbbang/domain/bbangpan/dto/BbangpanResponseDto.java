package nbbang.com.nbbang.domain.bbangpan.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.party.domain.Party;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class BbangpanResponseDto {

    private String bank;
    private String accountNumber;
    private Integer deliveryFee;
    private List<PartyMemberResponseDto> members;

    public static BbangpanResponseDto createDtoByParty(Party party) {
        return BbangpanResponseDto.builder()
                .bank(party.getBank())
                .accountNumber(party.getAccountNumber())
                .deliveryFee(party.getDeliveryFee())
                .members(party.getPartyMembers().stream().map(PartyMemberResponseDto::createDtoByEntity).collect(Collectors.toList()))
                .build();
    }

}
