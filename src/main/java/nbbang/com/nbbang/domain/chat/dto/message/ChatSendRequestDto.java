package nbbang.com.nbbang.domain.chat.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

import static nbbang.com.nbbang.domain.chat.controller.ChatResponseMessage.ILLEGAL_CHAT_LENGTH;

@Data @Builder
public class ChatSendRequestDto {
    @Size(min = 1, max = 1000, message = ILLEGAL_CHAT_LENGTH)
    private String content;
}
