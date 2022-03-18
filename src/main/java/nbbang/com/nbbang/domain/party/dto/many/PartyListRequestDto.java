package nbbang.com.nbbang.domain.party.dto.many;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.*;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.dto.many.PartyListRequestFilterDto;
import nbbang.com.nbbang.global.dto.PageableDto;
import nbbang.com.nbbang.global.support.validation.ValueOfEnum;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PartyListRequestDto extends PageableDto {

    @Parameter(description = "NONE / SINCHON / YEONHUI / CHANGCHEON, 예시: place=sinchon&place=NONE")
    private List<@ValueOfEnum(enumClass = Place.class) String> place;
    @Parameter(description = "OPEN / FULL / CLOSED, 예시: status=OPEN&status=full")
    private List<@ValueOfEnum(enumClass = PartyStatus.class) String> status;
    @Parameter(description = "제목 검색어")
    private String search;
    @Parameter(description = "커서 아이디를 정하지 않을 경우 가장 최근 파티 기준으로 조회합니다.")
    private Long cursorId;
    @Parameter(description = "해시태그를 포함하는 파티만 조회합니다.")
    private List<String> hashtags;
    @Parameter(description = "위시리스트에 포함된 파티만 검색합니다.")
    private Boolean isWishlist;

    @Parameter(hidden = true)
    public PartyListRequestFilterDto createPartyListRequestFilterDto() {
        return PartyListRequestFilterDto.builder()
                .search((search!=null)?search:null)
                .places((place!=null)?place.stream().map(p -> Place.valueOf(p.toUpperCase(Locale.ROOT))).collect(Collectors.toList()):null)
                .statuses((status!=null)?status.stream().map(s -> PartyStatus.valueOf(s.toUpperCase(Locale.ROOT))).collect(Collectors.toList()):null)
                .isWishlist((isWishlist!=null)?isWishlist:null)
                .build();
    }

}
