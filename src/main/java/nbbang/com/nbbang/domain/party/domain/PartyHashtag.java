package nbbang.com.nbbang.domain.party.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="hashtag_id")
    private Hashtag hashtag;

    protected PartyHashtag() {}
}
