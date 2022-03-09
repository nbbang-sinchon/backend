package nbbang.com.nbbang.domain.party.dto.single.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.validation.HashtagNumberAndDuplicate;
import nbbang.com.nbbang.domain.party.validation.PartyCreateGroup;
import nbbang.com.nbbang.global.support.validation.ValueOfEnum;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyRequestDto {

    @Parameter(description = "파티의 제목은 공백일 수 없습니다.")
    @NotBlank(message = "파티의 제목은 공백일 수 없습니다.", groups= PartyCreateGroup.class)
    private String title;

    private String content;

    @Parameter(description = "해시태그는 10 개 이하여야 하고, 중복이 없어야 합니다. List of string 으로 보내야 합니다.")
    @HashtagNumberAndDuplicate
    private List<String> hashtags;

    @Parameter(description = "NONE / SINCHON / CHANGCHEON / YEONHUI 중 하나여야 합니다.")
    @NotNull(message = "파티의 위치는 필수 값입니다.", groups= PartyCreateGroup.class)
    @ValueOfEnum(enumClass = Place.class)
    private String place;

    @Parameter(description = "2~10 이어야 합니다.")
    @NotNull(message = "모집 인원수는 필수 값입니다.", groups= PartyCreateGroup.class)
    @Min(value = 2, message = "모집 인원수는 2명 이상이어야 합니다")
    @Max(value=10, message = "모집 인원수는 10명 이하이어야 합니다")
    private Integer goalNumber;

    public Party createEntityByDto() {
        return Party.builder()
                .title(this.title)
                .content(this.content)
                .place(Place.valueOf(place.toUpperCase()))
                .goalNumber(this.goalNumber)
                .createTime(LocalDateTime.now())
                .build();
    }
}

