package nbbang.com.nbbang.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import nbbang.com.nbbang.global.support.validation.ValueOfEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MemberUpdateRequestDto {
    @Size(min = 1, max = 16) @NotNull
    private String nickname;
    @ValueOfEnum(enumClass = Place.class) @Schema(description = "올바른 place 값: SINCHON, ")
    private String place;
}
