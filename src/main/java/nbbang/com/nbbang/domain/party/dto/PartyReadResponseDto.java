package nbbang.com.nbbang.domain.party.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyReadResponseDto {
    private String title;
    private String content;
    private LocalDateTime createTime;
    private String place;
    private Integer joinNumber;
    private Integer goalNumber;
    private String status;
    private String nickname;
    private Integer breadNumber;
    private List<String> hashtags;
}