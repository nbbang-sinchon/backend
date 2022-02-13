package nbbang.com.nbbang.domain.chat.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.member.dto.MemberResponseDto;
import nbbang.com.nbbang.domain.member.dto.MemberResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data @Builder
public class ChatResponseDto {

    private String title;
    private String status;
    private Integer memberNumber;
    private Integer maxMemberNumber;
    private MemberResponseDto owner;
    private List<MemberResponseDto> participants;
    private LocalDateTime createTime;
    private List<ChatMessageResponseDto> chatMessages;

    public static ChatResponseDto createMock() {
        ChatResponseDto dto = ChatResponseDto.builder()
                .title("뿌링클 오늘 7시")
                .status("모집 중")
                .memberNumber(3)
                .maxMemberNumber(4)
                .owner(MemberResponseDto.createLuffy())
                .participants(Arrays.asList(MemberResponseDto.createKorung(), MemberResponseDto.createHyungKyung()))
                .createTime(LocalDateTime.of(2022, 02, 12, 12, 40))
                .chatMessages(Arrays.asList(ChatMessageResponseDto.createMock1(), ChatMessageResponseDto.createMock2()))
                .build();
        return dto;
    }
}