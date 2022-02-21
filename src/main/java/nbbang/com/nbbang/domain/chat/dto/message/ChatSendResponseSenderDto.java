package nbbang.com.nbbang.domain.chat.dto.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatSendResponseSenderDto {
    private Long id;
    private String nickName;
}
