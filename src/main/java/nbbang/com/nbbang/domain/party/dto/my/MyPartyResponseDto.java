package nbbang.com.nbbang.domain.party.dto.my;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Party;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPartyResponseDto{
    private Long id;
    private String title;
    private LocalDateTime createTime;
    private Integer joinNumber;
    private Integer goalNumber;
    private String status;
    private String place;
    private Boolean isOwner;
    private List<String> hashtags;

    public static MyPartyResponseDto createByEntity(Party party, Long memberId) {
        return MyPartyResponseDto.builder()
                .id(party.getId())
                .title(party.getTitle())
                .createTime(party.getCreateTime())
                .goalNumber(party.getGoalNumber())
                .joinNumber(party.getMemberParties().size() + 1)
                .status(party.getStatus()!=null?party.getStatus().toString():null)
                .hashtags(party.getHashtagContents())
                .isOwner(memberId == party.getOwner().getId())
                .place(party.getPlace().toString())
                .build();
    }
}