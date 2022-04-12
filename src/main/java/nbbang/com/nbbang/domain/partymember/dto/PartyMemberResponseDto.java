package nbbang.com.nbbang.domain.partymember.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.partymember.domain.PartyMember;

@Builder
@Data
public class PartyMemberResponseDto {

    private Long id;
    private Integer price;
    private Boolean isSent;
    private Boolean isOwner;
    private String nickname;
    private String avatar;

    public static PartyMemberResponseDto createDtoByEntity(PartyMember partyMember) {
        PartyMemberResponseDto partyMemberResponseDto = PartyMemberResponseDto.builder()
                .id(partyMember.getMember().getId())
                .price(partyMember.getPrice())
                .isSent(partyMember.getIsSent())
                .isOwner(partyMember.getMember().getId() == partyMember.getParty().getOwner().getId())
                .nickname(partyMember.getMember().getNickname())
                .avatar(partyMember.getMember().getAvatar())
                .build();
        return partyMemberResponseDto;
    }
}
