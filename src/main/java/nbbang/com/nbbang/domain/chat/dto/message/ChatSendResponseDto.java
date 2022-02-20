package nbbang.com.nbbang.domain.chat.dto.message;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.chat.domain.Message;

import java.time.LocalDateTime;
@Data
@Builder
public class ChatSendResponseDto {
    private String context;
    private Long senderId;
    private String senderNickName;
    private LocalDateTime createTime;

    public static ChatSendResponseDto createByMessage(Message message) {
        return ChatSendResponseDto.builder()
                .context(message.getContent())
                .senderId(message.getSender()!=null?message.getSender().getId():null)
                .senderNickName(message.getSender()!=null?message.getSender().getNickname():null)
                .createTime(message.getCreateTime())
                .build();
    }
}
