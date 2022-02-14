package nbbang.com.nbbang.domain.party.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter @Builder
@AllArgsConstructor
public class Hashtag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    protected Hashtag() {}
}
