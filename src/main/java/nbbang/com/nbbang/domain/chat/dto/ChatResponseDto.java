package nbbang.com.nbbang.domain.chat.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendResponseDto;
import nbbang.com.nbbang.domain.member.dto.MemberResponseDto;
import nbbang.com.nbbang.domain.party.domain.Party;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data @Builder
public class ChatResponseDto {
    private Long id;
    private String title;
    private LocalDateTime createTime;
    private Integer joinNumber;
    private Integer goalNumber;
    private String status;
    private MemberResponseDto owner;
    private List<MemberResponseDto> members;
    private ChatSendListResponseDto messages;

    public static ChatResponseDto createByPartyAndMessagesEntity(Party party, List<Message> messages, Long memberId) {
        return ChatResponseDto.builder()
                .id(party.getId())
                .title(party.getTitle())
                .createTime(party.getCreateTime())
                .owner(MemberResponseDto.createByEntity(party.getOwner()))
                .members(party.getPartyMembers().stream().map(m -> MemberResponseDto.createByEntity(m.getMember())).collect(Collectors.toList()))
                .goalNumber(party.getGoalNumber())
                .joinNumber(party.getPartyMembers().size())
                .status(party.getStatus().toString())
                .messages(ChatSendListResponseDto.createByEntity(messages, memberId))
                .build();
    }
}