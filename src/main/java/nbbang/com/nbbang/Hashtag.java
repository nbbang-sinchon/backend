package nbbang.com.nbbang;

import lombok.Getter;

import javax.persistence.*;

@Entity @Getter
public class Hashtag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;
}
