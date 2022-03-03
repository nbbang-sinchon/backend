package nbbang.com.nbbang.domain.bbangpan.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;
import nbbang.com.nbbang.domain.bbangpan.domain.SendStatus;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;

import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import static javax.persistence.EnumType.STRING;

@Builder
@Data
public class PartyMemberResponseDto {

    private Integer price;
    private SendStatus sendStatus;
    private Boolean isOwner;
    private String nickname;

    public static PartyMemberResponseDto createDtoByEntity(PartyMember partyMember) {
        PartyMemberResponseDto partyMemberResponseDto = PartyMemberResponseDto.builder()
                .price(partyMember.getPrice())
                .sendStatus(partyMember.getSendStatus())
                .isOwner(partyMember.getMember().getId() == partyMember.getParty().getOwner().getId())
                .nickname(partyMember.getMember().getNickname())
                .build();
        return partyMemberResponseDto;
    }
}
