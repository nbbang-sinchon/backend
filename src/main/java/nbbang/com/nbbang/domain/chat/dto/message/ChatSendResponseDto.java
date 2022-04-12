package nbbang.com.nbbang.domain.chat.dto.message;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.MessageType;

import java.time.LocalDateTime;

@Data
@Builder
@Slf4j
public class ChatSendResponseDto {
    private Long id;
    private LocalDateTime createTime;
    private Integer notReadNumber;
    private MessageType type;
    private String content;
    private Boolean isSender;
    private ChatSendResponseSenderDto sender;

    public static ChatSendResponseDto createByMessage(Message message) {
        return ChatSendResponseDto.builder()
                .id(message.getId())
                .createTime(message.getCreateTime())
                .notReadNumber(message.getNotReadNumber())
                .type(message.getType()!=null?message.getType():MessageType.CHAT)
                .content(message.getContent())
                .sender(message.getSender()!=null?ChatSendResponseSenderDto.create(message.getSender()):null)
                .build();
    }
}
