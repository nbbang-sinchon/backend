package nbbang.com.nbbang.domain.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReadMessageDto {
    private Long lastReadMessageId;
    private Long senderId;
}
