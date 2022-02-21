package nbbang.com.nbbang.domain.chat.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.member.dto.MemberResponseDto;
import nbbang.com.nbbang.domain.member.dto.MemberSimpleResponseDto;

import java.time.LocalDateTime;

@Data @Builder
public class ChatMessageResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdTime;
    private Integer notReadNumber;
    private String messageType;
    private MemberSimpleResponseDto sender;
    private Boolean isSender;

    public static ChatMessageResponseDto createByEntity(Message message) {
        return ChatMessageResponseDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .createdTime(message.getCreateTime())
                .notReadNumber(0)
                .messageType("MESSAGE")
                .sender(MemberSimpleResponseDto.createByEntity(message.getSender()))
                .build();
    }

    // 메시지 전송자와 조회자가 일치하는지
    public static ChatMessageResponseDto createByEntityAndMemberId(Message message, Long memberId) {
        return ChatMessageResponseDto.builder()
                .content(message.getContent())
                .createdTime(message.getCreateTime())
                .notReadNumber(0)
                .sender(MemberSimpleResponseDto.createByEntity(message.getSender()))
                .messageType("MESSAGE")
                .isSender(message.getSender().getId().equals(memberId))
                .build();
    }


}