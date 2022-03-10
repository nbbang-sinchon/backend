package nbbang.com.nbbang.domain.party.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;

import java.time.LocalDateTime;
import java.util.List;

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
    private Boolean isWishlist;
    private List<String> hashtags;

    public static PartyFindResponseDto createByEntity(Party party) {
        return PartyFindResponseDto.builder()
                .id(party.getId())
                .title(party.getTitle())
                .createTime(party.getCreateTime())
                .goalNumber(party.getGoalNumber())
                .joinNumber(party.getPartyMembers().size())
                .status(party.getStatus()!=null?party.getStatus().toString():null)
                .hashtags(party.getHashtagContents())
                .isWishlist(false)
                .place(party.getPlace().toString())
                .build();
    }

    public static PartyFindResponseDto createByEntity(Party party, Member member) {
        return PartyFindResponseDto.builder()
                .id(party.getId())
                .title(party.getTitle())
                .createTime(party.getCreateTime())
                .goalNumber(party.getGoalNumber())
                .joinNumber(party.getPartyMembers().size())
                .status(party.getStatus()!=null?party.getStatus().toString():null)
                .hashtags(party.getHashtagContents())
                .place(party.getPlace().toString())
                .isWishlist(party.isWishlistOf(member))
                .build();
    }


}