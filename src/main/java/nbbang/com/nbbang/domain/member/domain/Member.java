package nbbang.com.nbbang.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nbbang.com.nbbang.domain.member.dto.Place;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;

@Entity @Getter @Builder
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String avatar;

    @Enumerated(STRING)
    private Place place;

    protected Member() {}
}
