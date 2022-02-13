package nbbang.com.nbbang.domain.chat.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.member.dto.MemberResponseDto;

import java.time.LocalDateTime;

@Data @Builder
public class ChatMessageResponseDto {
    private String content;
    private LocalDateTime createdTime;
    private Integer notReadNumber;
    private String imagePath;
    private MemberResponseDto author;
    private Boolean isAuthor;

    public static ChatMessageResponseDto createMock1() {
        return ChatMessageResponseDto.builder()
                .content("저희 무슨 치킨시켜 먹나요?")
                .createdTime(LocalDateTime.of(2022, 02, 12, 12, 50))
                .notReadNumber(0)
                .author(MemberResponseDto.createKorung())
                .isAuthor(false)
                .build();

    }

    public static ChatMessageResponseDto createMock2() {
        return ChatMessageResponseDto.builder()
                .content("뿌링클 시키려고요 더 먹고싶은거 있으세요??")
                .createdTime(LocalDateTime.of(2022, 02, 12, 12, 50))
                .notReadNumber(1)
                .author(MemberResponseDto.createLuffy())
                .isAuthor(true)
                .build();
    }

}