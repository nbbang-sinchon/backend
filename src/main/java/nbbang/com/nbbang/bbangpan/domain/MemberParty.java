package nbbang.com.nbbang.bbangpan.domain;

import lombok.Getter;
import nbbang.com.nbbang.member.domain.Member;
import nbbang.com.nbbang.party.domain.Party;

import javax.persistence.*;

@Entity @Getter
public class MemberParty {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer price;

    private String sendStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;
}
