package nbbang.com.nbbang.domain.bbangpan.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.domain.Party;

import javax.persistence.*;

@Entity @Getter @Builder
@AllArgsConstructor
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

    protected MemberParty() {}
}
