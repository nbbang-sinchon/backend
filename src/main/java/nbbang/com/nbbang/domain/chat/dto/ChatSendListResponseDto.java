package nbbang.com.nbbang.domain.chat.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendResponseDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data @Builder
public class ChatSendListResponseDto {
    List<ChatSendResponseDto> messages;

    public static ChatSendListResponseDto createByEntity(List<Message> messages, Long memberId) {
        List<Message> ms = new ArrayList<>(messages);
        Collections.sort(ms);
        return ChatSendListResponseDto.builder()
                .messages(ms.stream()
                        .map(message -> ChatSendResponseDto.createByMessage(message))
                        .collect(Collectors.toList()))
                .build();
    }

}
