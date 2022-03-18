package nbbang.com.nbbang.domain.party.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity @Getter @Builder
@AllArgsConstructor
public class Hashtag {
    @Id @GeneratedValue
    @Column(name = "hashtag_id")
    private Long id;

    private String content;

    protected Hashtag() {}


    public static Hashtag createHashtag(String content) {
        Hashtag hashtag = Hashtag.builder().content(content).build();
        return hashtag;
    }

}
