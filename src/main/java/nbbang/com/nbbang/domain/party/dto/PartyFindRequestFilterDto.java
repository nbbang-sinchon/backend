package nbbang.com.nbbang.domain.party.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.global.dto.PageableDto;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Service layer 에 필터를 적용한 파티리스트 조회하기 위한 dto 입니다.
 */

@Data @Builder
public class PartyFindRequestFilterDto {

    @Builder.Default
    private Boolean ongoing = false;
    @Builder.Default
    private String search = "";
    private List<Place> places;

    public static PartyFindRequestFilterDto createRequestFilterDto(String search) {
        PartyFindRequestFilterDtoBuilder dtoBuilder = PartyFindRequestFilterDto.builder();
        if (search != null) {
            dtoBuilder.search(search);
        }
        return dtoBuilder.build();
    }

    public static PartyFindRequestFilterDto createRequestFilterDto(Boolean ongoing, String search) {
        PartyFindRequestFilterDtoBuilder dtoBuilder = PartyFindRequestFilterDto.builder();
        if (ongoing != null) {
            dtoBuilder.ongoing(ongoing);
        }
        if (search != null) {
            dtoBuilder.search(search);
        }
        return dtoBuilder.build();
    }

    public static PartyFindRequestFilterDto createRequestFilterDto(Boolean ongoing, String search, List<Place> places) {
        PartyFindRequestFilterDtoBuilder dtoBuilder = PartyFindRequestFilterDto.builder();
        if (ongoing != null) {
            dtoBuilder.ongoing(ongoing);
        }
        if (search != null) {
            dtoBuilder.search(search);
        }
        if (places != null) {
            dtoBuilder.places(places);
        }
        return dtoBuilder.build();
    }

    public static PartyFindRequestFilterDto createRequestFilterDto(PartyFindRequestDto requestDto) {
        return PartyFindRequestFilterDto.builder()
                .search(requestDto.getSearch())
                .build();
    }
}
