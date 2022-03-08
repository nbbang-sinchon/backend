package nbbang.com.nbbang.domain.bbangpan.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;

import javax.persistence.*;

import java.lang.reflect.Field;

import static javax.persistence.EnumType.STRING;

@Entity @Getter @Builder
@AllArgsConstructor
public class PartyMember {
    @Id @GeneratedValue
    private Long id;

    @Builder.Default
    private Integer price=0;

    @Enumerated(STRING)
    private SendStatus sendStatus;

    @OneToOne
    @JoinColumn(name = "message_id")
    private Message lastReadMessage = Message.builder().build();

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

    public void changePrice(Integer price) {
        this.price = price;
    }

    public void changeSendStatus(SendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public static Field getField(String field) throws NoSuchFieldException {
        return PartyMember.class.getDeclaredField(field);
    }
}
