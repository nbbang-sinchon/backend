package nbbang.com.nbbang.domain.chat.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ChatMessageSendRequestDto {
    @Size(max = 1000)
    private String content;
}