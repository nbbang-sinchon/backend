package nbbang.com.nbbang.domain.partymember.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;

import javax.persistence.*;

import java.lang.reflect.Field;

import static javax.persistence.FetchType.LAZY;

@Entity @Getter @Builder
@AllArgsConstructor
public class PartyMember {
    @Id @GeneratedValue
    private Long id;

    @Builder.Default
    private Integer price=0;

    @Builder.Default
    private Boolean isSent = false;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "message_id")
    @Builder.Default
    private Message lastReadMessage = Message.builder().build();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    protected PartyMember() {}

    public static PartyMember createPartyMember(Party party, Member member) {
        PartyMember partyMember = PartyMember.builder()
                .member(member)
                .party(party)
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

    public void changeIsSent(Boolean isSent) {
        this.isSent = isSent;
    }

    public static Field getField(String field) throws NoSuchFieldException {
        return PartyMember.class.getDeclaredField(field);
    }
}
