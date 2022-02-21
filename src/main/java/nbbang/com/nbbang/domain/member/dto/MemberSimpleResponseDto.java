package nbbang.com.nbbang.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSimpleResponseDto {
    private Long id;
    private String avatar;
    private String nickname;

    public static MemberSimpleResponseDto createByEntity(Member member) {
        return MemberSimpleResponseDto.builder()
                .id(member.getId())
                .avatar(member.getAvatar())
                .nickname(member.getNickname())
                .build();
    }
}
