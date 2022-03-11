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
    private Long destinationId;
    public static SocketSendDto createSocketSendDto(String type, Object data, Long destinationId){
        return SocketSendDto.builder().type(type).data(data).destinationId(destinationId).build();
    }
}
