package nbbang.com.nbbang.domain.member.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.member.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

@Data @Builder
public class MemberListResponseDto {
    List<MemberResponseDto> members;

    public static MemberListResponseDto createByEntity(List<Member> members) {
        return MemberListResponseDto.builder()
                .members( members.stream().map(m -> MemberResponseDto.createByEntity(m)).collect(Collectors.toList()) )
                .build();
    }
}
