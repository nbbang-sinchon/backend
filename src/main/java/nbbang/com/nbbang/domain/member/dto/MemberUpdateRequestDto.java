package nbbang.com.nbbang.domain.member.dto;

import lombok.Data;

@Data
public class MemberUpdateRequestDto {
    private String nickname;
    private String email;
}
