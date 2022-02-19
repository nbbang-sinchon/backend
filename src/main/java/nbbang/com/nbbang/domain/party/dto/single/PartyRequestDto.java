package nbbang.com.nbbang.domain.party.dto.single;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.validation.HashtagNumberAndDuplicate;
import nbbang.com.nbbang.global.support.validation.ValueOfEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyRequestDto {
    @NotBlank(message = "파티의 제목은 공백일 수 없습니다.")
    private String title;
    private String content;
    @HashtagNumberAndDuplicate
    private List<String> hashtags;
    @NotNull(message = "파티의 위치는 필수 값입니다.")
    @ValueOfEnum(enumClass = Place.class)
    private String place;
    @NotNull(message = "모집 인원수는 필수 값입니다.")
    private Integer goalNumber;

    public Party createByDto(){
        Party party = Party.builder()
                .title(this.title)
                .content(this.content)
                .place(Place.valueOf(place.toUpperCase()))
                .goalNumber(this.goalNumber)
                .isBlocked(false)
                .build();
        return party;
    }

    public Party createByDtoWithMember(Member owner) {
        return Party.builder()
                .title(this.title)
                .content(this.content)
                .place(Place.valueOf(place.toUpperCase()))
                .goalNumber(this.goalNumber)
                .owner(owner)
                .createTime(LocalDateTime.now())
                .build();
    }
}

