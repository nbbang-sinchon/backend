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

    private Boolean isLeaved;

    @Enumerated(STRING)
    private Place place;

    protected Member() {}

    public void updateMember(String nickname, Place place) {
        this.nickname = nickname;
        this.place = place;
    }

    public void leaveMember() {
        this.isLeaved = true;
    }

    public static Member createMember(String nickname, Place place) {
        return Member.builder()
                .nickname(nickname)
                .place(place)
                .isLeaved(false)
                .build();
    }


}
