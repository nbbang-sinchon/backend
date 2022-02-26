package nbbang.com.nbbang.domain.party.dto.many;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyFindResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Data @Builder
public class PartyListResponseDto {
    List<PartyFindResponseDto> parties;

    public static PartyListResponseDto createByEntity(List<Party> partyList) {
        return PartyListResponseDto.builder()
                .parties(partyList.stream().map(PartyFindResponseDto::createByEntity).collect(Collectors.toList()))
                .build();
    }

    public static PartyListResponseDto createByEntity(List<Party> partyList, Member member) {
        return PartyListResponseDto.builder()
                .parties(partyList.stream().map(p -> PartyFindResponseDto.createByEntity(p, member)).collect(Collectors.toList()))
                .build();
    }
}
