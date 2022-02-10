package nbbang.com.nbbang;

import lombok.Getter;

import javax.persistence.*;

@Entity @Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String avatar;

}
