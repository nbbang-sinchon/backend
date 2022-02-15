package nbbang.com.nbbang.domain.party.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.global.dto.PageableDto;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyFindRequestDto extends PageableDto {
    private List<String> places;
    private Boolean showOngoing;
    private String search;
}