package nbbang.com.nbbang.domain.party.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.*;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.global.dto.PageableDto;
import nbbang.com.nbbang.global.support.validation.ValueOfEnum;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class MyClosedPartyListRequestDto extends PageableDto {

    @Parameter(description = "제목 검색어")
    private String search;
    @Parameter(description = "커서 아이디를 정하지 않을 경우 가장 최근 파티 기준으로 조회합니다.")
    private Long cursorId;

    @Parameter(hidden = true)
    public PartyListRequestFilterDto createPartyListRequestFilterDto() {
        return PartyListRequestFilterDto.builder()
                .search((search != null)?search:null)
                .statuses(Arrays.asList(PartyStatus.CLOSED))
                .build();
    }
}
