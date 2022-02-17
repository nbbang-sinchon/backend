package nbbang.com.nbbang.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String profileImagePath;
    private String nickname;
    private String place;
    private Integer recommends;

    public static MemberResponseDto createByEntity(Member member) {
        MemberResponseDtoBuilder dtoBuilder = MemberResponseDto.builder();
        dtoBuilder.profileImagePath(member.getAvatar())
                .nickname(member.getNickname())
                .place(member.getPlace().toString());
        return dtoBuilder.build();
    }

    public static MemberResponseDto createMock() {
        return MemberResponseDto.builder()
                .profileImagePath("https://w.namu.la/s/bbff81cb4fd4d3f97d245a28a360b5cb665745b2cb434287f9cbd3423978919ce5377ef034f150277564c1798660ae95825fe2bfda50baa970f97d999a81c31401c0eb130e3bae0f9e1a2d3aea2a10769e564b0fbce08a8f23360382fd6e5425")
                .nickname("식빵좋아")
                .place("신촌동")
                .recommends(10)
                .build();
    }

    public static MemberResponseDto createKorung() {
        return MemberResponseDto.builder()
                .nickname("코렁")
                .place("신촌동")
                .recommends(10)
                .build();
    }

    public static MemberResponseDto createLuffy() {
        return MemberResponseDto.builder()
                .profileImagePath("https://w.namu.la/s/db854de68ffaa4ec688cc573a06d28d6b6e024c3fcd6160a6496103d0cacd5bfc3345a5dffc4c45b7591a97538ddc42280e0a57c9e2155ae90a836cff97436f10b1f8054e23a89ddd6f3d78fb58b99638995cfebd88e8994e3263a2895a2e73b")
                .nickname("루피")
                .place("신촌동")
                .recommends(10)
                .build();
    }

    public static MemberResponseDto createHyungKyung() {
        return MemberResponseDto.builder()
                .nickname("현경")
                .place("신촌동")
                .recommends(10)
                .build();
    }

}