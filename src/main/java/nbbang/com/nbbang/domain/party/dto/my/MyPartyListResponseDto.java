package nbbang.com.nbbang.domain.party.dto.my;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Party;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPartyListResponseDto {
    List<MyPartyResponseDto> parties;

    public static MyPartyListResponseDto createFromEntity(List<Party> partyList, Long memberId, List<Integer> notReadNumbers) {
        return MyPartyListResponseDto.builder()
                .parties(partyList.stream().map(p -> MyPartyResponseDto.createByEntity(p, memberId, notReadNumbers.get(partyList.indexOf(p)))).collect(Collectors.toList()))
                .build();
    }
    public static MyPartyListResponseDto createFromEntity(List<Party> partyList, Long memberId) {
        return MyPartyListResponseDto.builder()
                .parties(partyList.stream().map(p -> MyPartyResponseDto.createByEntity(p, memberId)).collect(Collectors.toList()))
                .build();
    }
}

