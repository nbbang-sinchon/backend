package nbbang.com.nbbang.domain.party.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.party.domain.Party;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data @Builder
public class PartyListResponseDto {
    List<PartyFindResponseDto> parties;

    public static PartyListResponseDto createFromEntity(List<Party> partyList) {
        return PartyListResponseDto.builder()
                .parties(partyList.stream().map(PartyFindResponseDto::createByEntity).collect(Collectors.toList()))
                .build();
    }

    public static PartyListResponseDto createMock() {
        return PartyListResponseDto.builder()
                .parties(Arrays.asList(PartyFindResponseDto.createMock()))
                .build();
    }
}
