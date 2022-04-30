package nbbang.com.nbbang.domain.hashtag.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbbang.com.nbbang.domain.party.domain.Party;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.*;

@Entity @Getter
@Builder
@AllArgsConstructor
public class PartyHashtag {
    @Id @GeneratedValue
    @Column(name = "party_hashtag_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="party_id")
    private Party party;


    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = LAZY)
    @JoinColumn(name="hashtag_id")
    private Hashtag hashtag;

    protected PartyHashtag() {}

    public String getContent() {
        return hashtag.getContent();
    }

    public static PartyHashtag createPartyHashtag(Party party, Hashtag hashtag) {
        PartyHashtag partyHashtag = PartyHashtag.builder().party(party).hashtag(hashtag).build();
        party.addPartyHashtag(partyHashtag);
        return partyHashtag;
    }

}
