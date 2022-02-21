package nbbang.com.nbbang.global.security;

import lombok.Getter;
import nbbang.com.nbbang.domain.member.domain.Member;

import java.io.Serializable;

@Getter
public class SessionMember implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String avatar;

    public SessionMember(Member member) {
        this.id = member.getId();
        this.name = member.getNickname();
        this.email = member.getEmail();
        this.avatar = member.getAvatar();
    }
}