package nbbang.com.nbbang.domain.party.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Party;

import javax.servlet.http.Part;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartyFindResponseDto{
    private Long id;
    private String title;
    private LocalDateTime createTime;
    private Integer joinNumber;
    private Integer goalNumber;
    private String status;
    private String place;
    private List<String> hashtags;

    public static PartyFindResponseDto createByEntity(Party party) {
        return PartyFindResponseDto.builder()
                .id(party.getId())
                .title(party.getTitle())
                .createTime(party.getCreateTime())
                .goalNumber(party.getGoalNumber())
                .joinNumber(party.getMemberParties().size() + 1)
                .status(party.getStatus()!=null?party.getStatus().toString():null)
                .hashtags(party.getHashtagContents())
                .place(party.getPlace().toString())
                .build();
    }
}