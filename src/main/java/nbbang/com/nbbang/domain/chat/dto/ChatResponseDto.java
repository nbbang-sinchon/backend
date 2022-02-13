package nbbang.com.nbbang.domain.chat.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.member.dto.MemberResponseDto;
import nbbang.com.nbbang.domain.member.dto.MemberResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
                .participants(new ArrayList<>())
                .createTime(LocalDateTime.of(2022, 02, 12, 12, 40))
                .chatMessages(new ArrayList<>())
                .build();

        dto.participants.add(MemberResponseDto.createKorung());
        dto.participants.add(MemberResponseDto.createHyungKyung());

        dto.chatMessages.add(ChatMessageResponseDto.createMock1());
        dto.chatMessages.add(ChatMessageResponseDto.createMock2());
        return dto;
    }
}