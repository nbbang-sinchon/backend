package nbbang.com.nbbang.domain.party.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.global.support.validation.ValueOfEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyRequestDto {
    @NotBlank(message = "파티의 제목은 공백일 수 없습니다.")
    private String title;
    private String content;
    private List<String> hashtags; // 해시태그 개수 검증기 만들기
    @NotNull(message = "파티의 위치는 필수 값입니다.")
    @ValueOfEnum(enumClass = Place.class)
    private String place;
    @NotNull(message = "모집 인원수는 필수 값입니다.")
    private Integer goalNumber;

    public Party createByDto(){
        Place place = Place.valueOf(this.place);
        Party party = Party.builder()
                .title(this.title)
                .content(this.content)
                .createTime(LocalDateTime.now())
                .place(place)
                .goalNumber(this.goalNumber)
                .build();
        return party;
    }

    public Party createByDto(Member owner) {
        return Party.builder()
                .title(this.title)
                .content(this.content)
                .place(Place.valueOf(this.place))
                .goalNumber(this.goalNumber)
                .owner(owner)
                .createTime(LocalDateTime.now())
                .build();
    }
}

