package nbbang.com.nbbang.domain.party.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.global.dto.PageableDto;
import nbbang.com.nbbang.global.support.validation.ValueOfEnum;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyFindRequestDto extends PageableDto {

    private List<String> places;
    @Parameter(description = "true -> ON 인 파티만, false(null) -> 모든 파티")
    private Boolean showOngoing;
    @Parameter(description = "제목 검색어")
    private String search;

    @Parameter(hidden = true)
    public List<Place> getPlaces() {
        if (places == null) {
            return null;
        }
        List<Place> res = new ArrayList<>();
        places.stream().forEach(p -> res.add(Place.valueOf(p)));
        return res;
    }

    public List<String> getPlacesString() {
        return places;
    }
}