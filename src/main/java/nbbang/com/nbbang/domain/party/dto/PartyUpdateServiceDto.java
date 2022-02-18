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
import java.util.Locale;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
public class PartyUpdateServiceDto {

    private Optional<String> title;
    private Optional<String> content;
    private Optional<List<String>> hashtagContents;
    private Optional<Place> place;
    private Optional<Integer> goalNumber;

    public static PartyUpdateServiceDto createByPartyRequestDto(PartyRequestDto partyRequestDto){
        return PartyUpdateServiceDto.builder()
                .title(Optional.ofNullable(partyRequestDto.getTitle()))
                .content(Optional.ofNullable(partyRequestDto.getContent()))
                .hashtagContents(Optional.ofNullable(partyRequestDto.getHashtags()))
                .place(Optional.ofNullable(partyRequestDto.getPlace()!=null?Place.valueOf(partyRequestDto.getPlace().toUpperCase(Locale.ROOT)):null))
                .goalNumber(Optional.ofNullable(partyRequestDto.getGoalNumber()))
                .build();
    }
}
