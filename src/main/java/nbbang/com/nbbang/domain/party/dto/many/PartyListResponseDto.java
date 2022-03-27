package nbbang.com.nbbang.domain.party.dto.many;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyFindResponseDto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class PartyListResponseDto implements Serializable {
    List<PartyFindResponseDto> parties;

    public static PartyListResponseDto createByEntity(List<Party> parties, Long memberId) {
        return PartyListResponseDto.builder()
                .parties(memberId != null ? parties.stream().map(p -> PartyFindResponseDto.createByEntity(p, memberId)).collect(Collectors.toList())
                        :parties.stream().map(PartyFindResponseDto::createByEntity).collect(Collectors.toList()))
                .build();
    }

}
