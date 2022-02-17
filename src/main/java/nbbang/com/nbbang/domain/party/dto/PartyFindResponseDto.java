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

    public static PartyFindResponseDto createMock() {
        return PartyFindResponseDto.builder()
                .title("연희동 올리브영 앞 BHC 7시")
                .createTime(LocalDateTime.now())
                .joinNumber(3)
                .goalNumber(4)
                .status("SOON")
                .hashtags(Arrays.asList("BHC", "치킨"))
                .build();
    }

    public static PartyFindResponseDto createByEntity(Party party) {
        return PartyFindResponseDto.builder()
                .id(party.getId())
                .title(party.getTitle())
                .createTime(party.getCreateTime())
                .goalNumber(party.getGoalNumber())
                .joinNumber(party.getMemberParties().size() + 1)
                .status(party.getStatus().toString())
                //.hashtags(party.getHashtags().stream().map(h -> h.getContent()).collect(Collectors.toList()))
                .place(party.getPlace().toString())
                .build();
    }
}