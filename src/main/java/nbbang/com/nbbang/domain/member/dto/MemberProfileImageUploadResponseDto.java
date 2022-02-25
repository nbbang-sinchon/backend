package nbbang.com.nbbang.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class MemberProfileImageUploadResponseDto {

    private String avatar;

    public static MemberProfileImageUploadResponseDto createByString(String urlPath) {
        return MemberProfileImageUploadResponseDto.builder()
                .avatar(urlPath)
                .build();
    }
}
