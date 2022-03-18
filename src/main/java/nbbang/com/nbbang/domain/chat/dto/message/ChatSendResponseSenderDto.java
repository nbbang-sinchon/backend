package nbbang.com.nbbang.domain.chat.dto.message;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import nbbang.com.nbbang.domain.member.domain.Member;

@Data
@Builder(access = AccessLevel.PROTECTED)
public class ChatSendResponseSenderDto {
    private Long id;
    private String nickname;
    private String avatar;
    public static ChatSendResponseSenderDto create(Member member){
        return ChatSendResponseSenderDto.builder().id(member.getId())
                .nickname(member.getNickname()).avatar(member.getAvatar()).build();
    }
}

