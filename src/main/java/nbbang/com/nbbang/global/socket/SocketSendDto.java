package nbbang.com.nbbang.global.socket;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SocketSendDto {
    private String type;
    private Object data;
    public static SocketSendDto createSocketSendDto(String type, Object data){
        return SocketSendDto.builder().type(type).data(data).build();
    }
}
