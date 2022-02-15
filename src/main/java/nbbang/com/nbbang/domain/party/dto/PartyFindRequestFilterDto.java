package nbbang.com.nbbang.domain.party.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.global.dto.PageableDto;

import java.awt.print.Pageable;

/**
 * Service layer 에 필터를 적용한 파티리스트 조회하기 위한 dto 입니다.
 */

@Data @Builder
public class PartyFindRequestFilterDto {

    @Builder.Default
    private Boolean showOngoing = false;
    @Builder.Default
    private String search = "";

    public static PartyFindRequestFilterDto createRequestFilterDto(String search) {
        PartyFindRequestFilterDtoBuilder dtoBuilder = PartyFindRequestFilterDto.builder();
        if (search != null) {
            dtoBuilder.search(search);
        }
        return dtoBuilder.build();
    }

    public static PartyFindRequestFilterDto createRequestFilterDto(Boolean showOngoing, String search) {
        PartyFindRequestFilterDtoBuilder dtoBuilder = PartyFindRequestFilterDto.builder();
        if (showOngoing != null) {
            dtoBuilder.showOngoing(showOngoing);
        }
        if (search != null) {
            dtoBuilder.search(search);
        }
        return dtoBuilder.build();
    }

    public static PartyFindRequestFilterDto createRequestFilterDto(PartyFindRequestDto requestDto) {
        return PartyFindRequestFilterDto.builder()
                .search(requestDto.getSearch())
                .build();
    }
}
