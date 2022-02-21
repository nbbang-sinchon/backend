package nbbang.com.nbbang.domain.chat.dto.message;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.MessageType;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.MemberDto;
import nbbang.com.nbbang.domain.member.dto.MemberSimpleResponseDto;
import nbbang.com.nbbang.domain.party.domain.Party;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
public class ChatSendResponseDto {
    private Long id;
    private LocalDateTime createTime;
    private Integer readNumber;
    private MessageType type;
    private String content;
    private ChatSendResponseSenderDto sender;


    public static ChatSendResponseDto createByMessage(Message message) {
        return ChatSendResponseDto.builder()
                .id(message.getId())
                .createTime(message.getCreateTime())
                .readNumber(message.getReadNumber())
                .type(message.getType())
                .content(message.getContent())
                .sender(message.getSender()!=null?
                        ChatSendResponseSenderDto.builder().id(message.getSender().getId())
                                .nickName(message.getSender().getNickname()).build() :null)
                .build();
    }

}
