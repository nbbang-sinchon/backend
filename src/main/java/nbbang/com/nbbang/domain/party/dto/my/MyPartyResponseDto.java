package nbbang.com.nbbang.domain.party.dto.my;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.member.dto.MemberSimpleResponseDto;
import nbbang.com.nbbang.domain.party.domain.Party;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private MemberSimpleResponseDto owner;
    private Boolean isOwner;
    private List<String> hashtags;
    private List<MemberSimpleResponseDto> members;
    private Integer notReadNumber;

    public static MyPartyResponseDto createByEntity(Party party, Long memberId) {
        return MyPartyResponseDto.builder()
                .id(party.getId())
                .title(party.getTitle())
                .createTime(party.getCreateTime())
                .goalNumber(party.getGoalNumber())
                .joinNumber(party.getPartyMembers().size() + 1)
                .owner(MemberSimpleResponseDto.createByEntity(party.getOwner()))
                .status(party.getStatus()!=null?party.getStatus().toString():null)
                .hashtags(party.getHashtagContents())
                .isOwner(memberId == party.getOwner().getId())
                .members(party.getPartyMembers().stream().map(mp -> MemberSimpleResponseDto.createByEntity(mp.getMember())).collect(Collectors.toList()))
                .place(party.getPlace().toString())
                .build();
    }

    public static MyPartyResponseDto createByEntity(Party party, Long memberId, Integer notReadNumber) {
        return MyPartyResponseDto.builder()
                .id(party.getId())
                .title(party.getTitle())
                .createTime(party.getCreateTime())
                .goalNumber(party.getGoalNumber())
                .joinNumber(party.getPartyMembers().size() + 1)
                .owner(MemberSimpleResponseDto.createByEntity(party.getOwner()))
                .status(party.getStatus()!=null?party.getStatus().toString():null)
                .hashtags(party.getHashtagContents())
                .isOwner(memberId == party.getOwner().getId())
                .members(party.getPartyMembers().stream().map(mp -> MemberSimpleResponseDto.createByEntity(mp.getMember())).collect(Collectors.toList()))
                .place(party.getPlace().toString())
                .notReadNumber(notReadNumber)
                .build();
    }
}