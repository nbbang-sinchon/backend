package nbbang.com.nbbang.domain.party.dto.single.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Party;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyIdResponseDto {
    private Long id;
    public static PartyIdResponseDto createByParty(Party party){
        return PartyIdResponseDto.builder().id(party.getId()).build();
    }
}
