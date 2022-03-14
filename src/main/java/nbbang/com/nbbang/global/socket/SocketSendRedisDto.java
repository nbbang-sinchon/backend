package nbbang.com.nbbang.global.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketSendRedisDto {
    private String type;
    private Object data;
    private String topic;
    public static SocketSendRedisDto createSocketSendDto(String type, Object data, String topic){
        return SocketSendRedisDto.builder().type(type).data(data).topic(topic).build();
    }
}
