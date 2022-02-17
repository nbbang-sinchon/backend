package nbbang.com.nbbang.domain.party.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.Hashtag;
import nbbang.com.nbbang.global.support.validation.ValueOfEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PartyUpdateServiceDto {

    private String title;
    private String content;
    private List<String> hashtagContents;
    private Place place;
    private Integer goalNumber;

    public static PartyUpdateServiceDto createByPartyRequestDto(PartyRequestDto partyRequestDto){
        return PartyUpdateServiceDto.builder()
                .title(partyRequestDto.getTitle())
                .content(partyRequestDto.getContent())
                .hashtagContents(partyRequestDto.getHashtags())
                .place(Place.valueOf(partyRequestDto.getPlace()))
                .goalNumber(partyRequestDto.getGoalNumber())
                .build();
    }
}
