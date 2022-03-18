package nbbang.com.nbbang.global.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketSendDto {
    private String type;
    private Object data;
    public static SocketSendDto create(SocketSendRedisDto socketSendRedisDto){
        return SocketSendDto.builder().type(socketSendRedisDto.getType()).data(socketSendRedisDto.getData()).build();
    }
}
