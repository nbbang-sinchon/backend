package nbbang.com.nbbang.domain.chat.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Data @Builder
public class ChatSendListResponseDto {
    List<ChatSendResponseDto> messages;

    public static ChatSendListResponseDto createByEntity(List<Message> messages) {
        return ChatSendListResponseDto.builder()
                .messages(messages.stream()
                        .map(ChatSendResponseDto::createByMessage)
                        .collect(Collectors.toList()))
                .build();
    }

    public static ChatSendListResponseDto createByEntityAndMemberId(List<Message> messages, Long memberId) {
        return ChatSendListResponseDto.builder()
                .messages(messages.stream()
                        .map((m) ->
                            ChatSendResponseDto.createByMessage(m)
                        )
                        .collect(Collectors.toList()))
                .build();
    }
}
