package nbbang.com.nbbang.domain.chat.dto.message;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.domain.MessageType;
import nbbang.com.nbbang.domain.party.domain.Party;

import java.time.LocalDateTime;

@Data
@Builder
@Slf4j
public class ChatAlarmResponseDto {
    private Long id;
    private LocalDateTime createTime;
    private MessageType type;
    private String content;
    private ChatSendResponseSenderDto sender;
    private ChatAlarmPartyDto party;

    public static ChatAlarmResponseDto create(Party party, ChatSendResponseDto chatSendResponseDto) {
        return ChatAlarmResponseDto.builder()
                .id(chatSendResponseDto.getId())
                .party(ChatAlarmPartyDto.createByParty(party))
                .createTime(chatSendResponseDto.getCreateTime())
                .type(chatSendResponseDto.getType())
                .content(chatSendResponseDto.getContent())
                .sender(chatSendResponseDto.getSender())
                .build();
    }
}
