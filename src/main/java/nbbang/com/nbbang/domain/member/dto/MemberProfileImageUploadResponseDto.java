package nbbang.com.nbbang.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class MemberProfileImageUploadResponseDto {

    private String profileImagePath;

    public static MemberProfileImageUploadResponseDto createMock() {
        return MemberProfileImageUploadResponseDto.builder()
                .profileImagePath("https://w.namu.la/s/bbff81cb4fd4d3f97d245a28a360b5cb665745b2cb434287f9cbd3423978919ce5377ef034f150277564c1798660ae95825fe2bfda50baa970f97d999a81c31401c0eb130e3bae0f9e1a2d3aea2a10769e564b0fbce08a8f23360382fd6e5425")
                .build();
    }
}
