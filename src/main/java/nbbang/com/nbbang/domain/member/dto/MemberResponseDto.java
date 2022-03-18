package nbbang.com.nbbang.domain.member.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import nbbang.com.nbbang.domain.member.domain.Member;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    @Parameter(description = "프로필 사진 uri 입니다. 설정되지 않았을 경우 null 을 리턴합니다.")
    private String avatar;
    private String nickname;
    private Integer breadNumber;
    private String place;
    private Boolean isThereNotReadMessage;

    public static MemberResponseDto createByEntity(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .avatar(member.getAvatar()!=null?member.getAvatar():null)
                .nickname(member.getNickname())
                .place(member.getPlace()!=null?member.getPlace().toString():Place.NONE.toString())
                .build();
    }

    public static MemberResponseDto createByEntity(Member member, Boolean isThereNotReadMessage) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .avatar(member.getAvatar()!=null?member.getAvatar():null)
                .nickname(member.getNickname())
                .place(member.getPlace()!=null?member.getPlace().toString():Place.NONE.toString())
                .isThereNotReadMessage(isThereNotReadMessage)
                .build();
    }
}