package nbbang.com.nbbang.domain.party.dto.many;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;

import java.util.List;

@Data @Builder
public class PartyListRequestFilterDto {
    private List<PartyStatus> statuses;
    private String search;
    private List<Place> places;
    private Boolean isWishlist;
}
