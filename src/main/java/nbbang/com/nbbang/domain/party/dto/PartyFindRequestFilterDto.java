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

    private String search;

    public static PartyFindRequestFilterDto createRequestFilterDto(String search) {
        return PartyFindRequestFilterDto.builder()
                .search(search)
                .build();
    }

    public static PartyFindRequestFilterDto createRequestFilterDto(PartyFindRequestDto requestDto) {
        return PartyFindRequestFilterDto.builder()
                .search(requestDto.getSearch())
                .build();
    }
}
