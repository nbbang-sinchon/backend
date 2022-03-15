package nbbang.com.nbbang.domain.party.dto.single.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.member.dto.MemberResponseDto;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyFindResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartyReadResponseDto {
    private MemberResponseDto owner;
    private String place;
    private String status;
    private Integer joinNumber;
    private Integer goalNumber;
    private LocalDateTime createTime;
    private List<String> hashtags;
    private Boolean isOwner;
    private Boolean isMember;
    private String title;
    private String content;
    private Boolean isWishlist;
    private List<PartyFindResponseDto> parties;

    public static PartyReadResponseDto createDto(Party party, Long userId, List<String> hashtags,  List<PartyFindResponseDto> parties) {
        /* 유저 정보(닉네임, 빵 수), 지역, 모집 현황(참여 인원수/모집 인원수, 모집 상태), 작성 시간, 해시태그, 하트 수,
        작성자와 조회자 일치 여부, 파티 참여 여부, 제목, 내용, 최근 파티 목록을 제공한다.
        */
        boolean isMember = (userId!=null?party.getPartyMembers().stream()
                .anyMatch(memberParty -> memberParty.getMember().getId().equals(userId)):false);
        PartyReadResponseDto partyReadResponseDto =  PartyReadResponseDto.builder()
                .owner(MemberResponseDto.createByEntity(party.getOwner()))
                .place(party.getPlace().name())
                .status((party.getStatus()!=null)?party.getStatus().name():null)
                .joinNumber((party.getPartyMembers()!=null)?party.getPartyMembers().size():null)
                .goalNumber(party.getGoalNumber())
                .createTime(party.getCreateTime())
                .hashtags(hashtags)
                .isOwner((party.getOwner()!=null && userId!=null)?((party.getOwner().getId())==userId):false)
                .isMember(isMember)
                .isWishlist(userId!=null?party.isWishlistOf(userId):false)
                .title(party.getTitle())
                .content(party.getContent())
                .parties(parties)
                .build();
        return partyReadResponseDto;
    }
}