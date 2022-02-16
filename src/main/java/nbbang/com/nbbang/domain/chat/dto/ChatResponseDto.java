package nbbang.com.nbbang.domain.chat.dto;

import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.member.dto.MemberResponseDto;
import nbbang.com.nbbang.domain.member.dto.MemberResponseDto;
import nbbang.com.nbbang.domain.party.domain.Party;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data @Builder
public class ChatResponseDto {

    private String title;
    private LocalDateTime createTime;
    private Integer joinNumber;
    private Integer goalNumber;
    private String status;
    private MemberResponseDto owner;
    private List<MemberResponseDto> participants;
    private Long lastMessageId;

    public static ChatResponseDto createByPartyEntity(Party party, Long lastMessageId) {
        return ChatResponseDto.builder()
                .title(party.getTitle())
                .createTime(party.getCreateTime())
                .owner(MemberResponseDto.createByEntity(party.getOwner()))
                .participants(party.getMemberParties().stream().map(m -> MemberResponseDto.createByEntity(m.getMember())).collect(Collectors.toList()))
                .goalNumber(party.getGoalNumber())
                .joinNumber(party.getMemberParties().size())
                .status(party.getStatus().toString())
                .lastMessageId(lastMessageId)
                .build();
    }

    public static ChatResponseDto createMock() {
        ChatResponseDto dto = ChatResponseDto.builder()
                .title("뿌링클 오늘 7시")
                .status("모집 중")
                .joinNumber(3)
                .goalNumber(4)
                .owner(MemberResponseDto.createLuffy())
                .participants(Arrays.asList(MemberResponseDto.createKorung(), MemberResponseDto.createHyungKyung()))
                .createTime(LocalDateTime.of(2022, 02, 12, 12, 40))
                //.chatMessages(Arrays.asList(ChatMessageResponseDto.createMock1(), ChatMessageResponseDto.createMock2()))
                .build();
        return dto;
    }
}