package nbbang.com.nbbang.domain.bbangpan.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;

import javax.persistence.*;

@Entity @Getter @Builder
@AllArgsConstructor
public class PartyMember {
    @Id @GeneratedValue
    private Long id;

    private Integer price;

    private String sendStatus;

    @OneToOne
    @JoinColumn(name = "message_id")
    private Message lastReadMessage;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    protected PartyMember() {}

    public static PartyMember createMemberParty(Member member, Party party, Message lastReadMessage) {
        PartyMember partyMember = PartyMember.builder()
                .member(member)
                .party(party)
                .lastReadMessage(lastReadMessage)
                .build();
        party.getPartyMembers().add(partyMember);
        return partyMember;
    }

    public void changeLastReadMessage(Message message) {
        this.lastReadMessage = message;
    }
}
