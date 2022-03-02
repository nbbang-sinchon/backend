package nbbang.com.nbbang.domain.bbangpan.dto;

import lombok.Builder;
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
public class PartyMemberResponseDto {
/*    public static PartyMemberResponseDto createDto(PartyMember partyMember) {
        // PartyMemberResponseDto partyMemberResponseDto = PartyMemberResponseDto.builder().build();

        // 유저별(나인지, 파티장인지, 그냥인지) 닉네임, 금액, 송금여부,
        @Builder.Default
        private Integer price=0;

        @Enumerated(STRING)
        private SendStatus sendStatus;

        @OneToOne
        @JoinColumn(name = "message_id")
        private Message lastReadMessage;

        @ManyToOne
        @JoinColumn(name = "member_id")
        private Member member;

        @ManyToOne
        @JoinColumn(name = "party_id")
        private Party party;


        return partyMemberResponseDto;
    }*/
}
