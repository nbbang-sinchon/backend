package nbbang.com.nbbang.global.socket.redisPubSub;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.socket.SocketSendDto;
import nbbang.com.nbbang.global.socket.SocketSendRedisDto;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            SocketSendRedisDto data = objectMapper.readValue(publishMessage, SocketSendRedisDto.class);
            SocketSendDto socketSendDto = SocketSendDto.create(data);
            messagingTemplate.convertAndSend(data.getTopic(), socketSendDto);
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
