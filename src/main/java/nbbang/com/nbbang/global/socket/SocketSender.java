package nbbang.com.nbbang.global.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.cache.PartyMemberCacheService;
import nbbang.com.nbbang.global.cache.PartyMemberIdCache;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.dto.message.ChatAlarmResponseDto;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendResponseDto;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.global.socket.redisPubSub.RedisPublisher;
import nbbang.com.nbbang.global.socket.redisPubSub.RedisTopicRepository;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import java.util.List;

import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.PARTY_NOT_FOUND;
import static nbbang.com.nbbang.global.socket.SocketDestination.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketSender {
    private final PartyRepository partyRepository;
    private final RedisPublisher redisPublisher;
    private final RedisTopicRepository redisTopicRepository;
    private final PartyMemberCacheService partyMemberCacheService;

    public void sendChattingByMessage(Message message){
        ChatSendResponseDto chatSendResponseDto = ChatSendResponseDto.createByMessage(message);
        Long partyId = message.getParty().getId();
        send(CHATTING, partyId,  chatSendResponseDto);
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NotFoundException(PARTY_NOT_FOUND));
        ChatAlarmResponseDto chatAlarmResponseDto = ChatAlarmResponseDto.create(party, chatSendResponseDto);

        List<PartyMemberIdCache> partyMembers = partyMemberCacheService.getPartyMembersCacheByPartyId(partyId);
        partyMembers.stream().forEach(pm -> sendGlobal(pm.getMemberId(), chatAlarmResponseDto));
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
