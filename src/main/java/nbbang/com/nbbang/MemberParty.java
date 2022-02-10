package nbbang.com.nbbang;

import lombok.Getter;

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
