package nbbang.com.nbbang.domain.party.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyRequestDto {
    @NotBlank(message = "파티의 제목은 공백일 수 없습니다.")
    private String title;
    private String content;
    private List<String> hashtags; // 해시태그 개수 검증기 만들기
    @NotBlank(message = "파티의 위치는 필수 값입니다.")
    private String place;
    @NotNull(message = "모집 인원수는 필수 값입니다.")
    private Integer goalNumber;
}
