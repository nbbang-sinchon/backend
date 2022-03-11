package nbbang.com.nbbang.global.socket;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SocketSendDto {
    private String type;
    private Object data;
    private Long destinationId;
    public static SocketSendDto createSocketSendDto(String type, Object data, Long destinationId){
        return SocketSendDto.builder().type(type).data(data).destinationId(destinationId).build();
    }
}
