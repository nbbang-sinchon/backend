package nbbang.com.nbbang.global.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.dto.message.ChatAlarmResponseDto;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendResponseDto;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.socket.redisPubSub.RedisPublisher;
import nbbang.com.nbbang.global.socket.redisPubSub.RedisTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static nbbang.com.nbbang.global.socket.SocketDestination.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketSender {
    private final PartyService partyService;
    private final RedisPublisher redisPublisher;
    private final RedisTopicRepository redisTopicRepository;

    public void sendChattingByMessage(Message message){
        ChatSendResponseDto chatSendResponseDto = ChatSendResponseDto.createByMessage(message);
        Long partyId = message.getParty().getId();
        send(CHATTING, partyId,  chatSendResponseDto);
        List<Member> members = partyService.findMembers(partyId);
        ChatAlarmResponseDto chatAlarmResponseDto = ChatAlarmResponseDto.create(partyService.findById(partyId), chatSendResponseDto);
        members.stream().forEach(member -> sendGlobal(member.getId(),chatAlarmResponseDto));
    }

    public void sendChattingReadMessage(Long partyId, Object data){
        send(CHATTING, partyId, "reading", data);
    }
    public void sendBreadBoard(Long partyId, Object data){
        send(BREAD_BOARD, partyId, data);
    }
    public void sendGlobal(Long memberId, Object data){
        send(GLOBAL, memberId, data);
    }

    public void send(String destination, Long id, Object data){
        send(destination,id, destination, data );
    }

    public void send(String destination, Long id, String type, Object data){
        String topic = "/topic/"+destination+"/"+id;
        SocketSendRedisDto socketSendRedisDto = SocketSendRedisDto.createSocketSendDto(type, data, topic);
        redisPublisher.publish(redisTopicRepository.getTopic(topic), socketSendRedisDto);
    }
}
