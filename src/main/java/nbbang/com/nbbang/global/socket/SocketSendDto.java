package nbbang.com.nbbang.global.socket;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SocketSendDto {
    private Object data;
    public static SocketSendDto createSocketSendDto(Object data){
        return SocketSendDto.builder().data(data).build();
    }
}
