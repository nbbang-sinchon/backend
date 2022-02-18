package nbbang.com.nbbang.domain.party.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.*;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.global.dto.PageableDto;
import nbbang.com.nbbang.global.support.validation.ValueOfEnum;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PartyFindRequestDto extends PageableDto {

    private List<String> places;
    @Parameter(description = "true -> OPEN 인 파티만, false(null) -> 모든 파티")
    private Boolean isOngoing;
    @Parameter(description = "제목 검색어")
    private String search;

    @Parameter(hidden = true)
    public List<Place> getPlaces() {
        if (places == null) {
            return null;
        }
        return places.stream()
                .map(p -> Place.valueOf(p.toUpperCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }

    public List<String> getPlacesString() {
        return places;
    }
}