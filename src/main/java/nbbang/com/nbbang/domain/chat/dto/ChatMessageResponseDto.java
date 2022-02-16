package nbbang.com.nbbang.domain.chat.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.chat.domain.Message;
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

    public static ChatMessageResponseDto createByEntity(Message message) {
        return ChatMessageResponseDto.builder()
                .content(message.getContent())
                .createdTime(message.getCreateTime())
                .notReadNumber(0)
                .author(MemberResponseDto.createByEntity(message.getAuthor()))
                .build();
    }

    // 메시지 전송자와 조회자가 일치하는지
    public static ChatMessageResponseDto createByEntityAndMemberId(Message message, Long memberId) {
        return ChatMessageResponseDto.builder()
                .content(message.getContent())
                .createdTime(message.getCreateTime())
                .notReadNumber(0)
                .author(MemberResponseDto.createByEntity(message.getAuthor()))
                .isAuthor(message.getAuthor().getId().equals(memberId))
                .build();
    }

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