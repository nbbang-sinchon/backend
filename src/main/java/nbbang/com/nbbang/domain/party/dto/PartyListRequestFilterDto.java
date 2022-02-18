package nbbang.com.nbbang.domain.party.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;

import java.util.List;

@Data @Builder
public class PartyListRequestFilterDto {
    private PartyStatus status;
    private String search;
    private List<Place> places;
}
