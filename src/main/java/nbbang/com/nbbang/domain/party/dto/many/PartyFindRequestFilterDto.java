package nbbang.com.nbbang.domain.party.dto.many;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.member.dto.Place;

import java.util.List;
/**
 * Service layer 에 필터를 적용한 파티리스트 조회하기 위한 dto 입니다.
 */

@Data @Builder
public class PartyFindRequestFilterDto {

    @Builder.Default
    private Boolean isOngoing = false;
    @Builder.Default
    private String search = "";
    private List<Place> places;

    public static PartyFindRequestFilterDto createRequestFilterDto() {
        PartyFindRequestFilterDtoBuilder dtoBuilder = PartyFindRequestFilterDto.builder();
        return dtoBuilder.build();
    }

    public static PartyFindRequestFilterDto createRequestFilterDto(String search) {
        PartyFindRequestFilterDtoBuilder dtoBuilder = PartyFindRequestFilterDto.builder();
        if (search != null) {
            dtoBuilder.search(search);
        }
        return dtoBuilder.build();
    }

    public static PartyFindRequestFilterDto createRequestFilterDto(Boolean isOngoing, String search) {
        PartyFindRequestFilterDtoBuilder dtoBuilder = PartyFindRequestFilterDto.builder();
        if (isOngoing != null) {
            dtoBuilder.isOngoing(isOngoing);
        }
        if (search != null) {
            dtoBuilder.search(search);
        }
        return dtoBuilder.build();
    }

    public static PartyFindRequestFilterDto createRequestFilterDto(Boolean isOngoing, String search, List<Place> places) {
        PartyFindRequestFilterDtoBuilder dtoBuilder = PartyFindRequestFilterDto.builder();
        if (isOngoing != null) {
            dtoBuilder.isOngoing(isOngoing);
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
