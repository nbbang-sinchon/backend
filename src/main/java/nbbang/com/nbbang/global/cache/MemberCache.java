package nbbang.com.nbbang.global.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberCache {
    private Long id;
    private String nickname;
    private String avatar;

    public Member createMember(){
        return Member.builder().id(this.id).avatar(this.avatar).nickname(this.nickname).build();
    }

}
