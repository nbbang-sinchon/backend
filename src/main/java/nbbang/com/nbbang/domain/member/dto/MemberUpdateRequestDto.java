package nbbang.com.nbbang.domain.member.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import nbbang.com.nbbang.global.support.validation.ValueOfEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Locale;

@Data
public class MemberUpdateRequestDto {
    @Size(min = 1, max = 16) @NotNull
    private String nickname;
    @Parameter(description = "위치 정보를 입력합니다. ")
    @ValueOfEnum(enumClass = Place.class) @Schema(implementation = Place.class)
    private String place;
    public Place getPlace() {
        if (place == null) {
            return Place.NONE;
        }
        return Place.valueOf(place.toUpperCase(Locale.ROOT));
    }
}
