package nbbang.com.nbbang.domain.breadboard.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.partymember.dto.PartyMemberResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class BreadBoardResponseDto {

    private String bank;
    private String accountNumber;
    private Integer deliveryFee;
    private List<PartyMemberResponseDto> members;

    public static BreadBoardResponseDto createDtoByParty(Party party) {
        return BreadBoardResponseDto.builder()
                .bank(party.getBank())
                .accountNumber(party.getAccountNumber())
                .deliveryFee(party.getDeliveryFee())
                .members(party.getPartyMembers().stream().map(PartyMemberResponseDto::createDtoByEntity).collect(Collectors.toList()))
                .build();
    }

}
