package nbbang.com.nbbang.global.socket.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocketReadMessageDto {
    private Long lastReadMessageId;
    private Long senderId;
}
