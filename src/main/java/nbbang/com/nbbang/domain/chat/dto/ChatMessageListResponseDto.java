package nbbang.com.nbbang.domain.chat.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.member.dto.Place;

import java.util.List;
import java.util.stream.Collectors;

@Data @Builder
public class ChatMessageListResponseDto {
    List<ChatMessageResponseDto> messages;

    public static ChatMessageListResponseDto createByEntity(List<Message> messages) {
        /*ChatMessageListResponseDtoBuilder dto = ChatMessageListResponseDto.builder();
        dto.messages(messages.stream()
                .map(m -> ChatMessageResponseDto.createByEntity(m))
                .collect(Collectors.toList()));
        return dto.build();*/
        return ChatMessageListResponseDto.builder()
                .messages(messages.stream()
                        .map(ChatMessageResponseDto::createByEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    public static ChatMessageListResponseDto createByEntityAndMemberId(List<Message> messages, Long memberId) {
        return ChatMessageListResponseDto.builder()
                .messages(messages.stream()
                        .map((m) -> ChatMessageResponseDto.createByEntityAndMemberId(m, memberId))
                        .collect(Collectors.toList()))
                .build();
    }
}
