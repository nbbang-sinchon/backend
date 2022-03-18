package nbbang.com.nbbang.domain.party.dto.my;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.*;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.dto.many.PartyListRequestFilterDto;
import nbbang.com.nbbang.global.dto.PageableDto;

import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MyOnPartyListRequestDto extends PageableDto {

    @Parameter(description = "제목 검색어")
    private String search;
    @Parameter(description = "커서 아이디를 정하지 않을 경우 가장 최근 파티 기준으로 조회합니다.")
    private Long cursorId;

    @Parameter(hidden = true)
    public PartyListRequestFilterDto createPartyListRequestFilterDto() {
        return PartyListRequestFilterDto.builder()
                .search((search != null)?search:null)
                .statuses(Arrays.asList(PartyStatus.OPEN, PartyStatus.FULL))
                .build();
    }
}