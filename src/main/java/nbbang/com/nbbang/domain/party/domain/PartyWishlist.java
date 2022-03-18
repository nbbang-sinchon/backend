package nbbang.com.nbbang.domain.party.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbbang.com.nbbang.domain.member.domain.Member;

import javax.persistence.*;
import javax.servlet.http.Part;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class PartyWishlist {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "wishlist_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    protected PartyWishlist() { }

    public static PartyWishlist createPartyWishlist(Member member, Party party) {
        return PartyWishlist.builder()
                .member(member)
                .party(party)
                .build();
    }
}
