package nbbang.com.nbbang.domain.chat.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.dto.ChatRoom;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.global.socket.RedisSubscriber;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
@Slf4j
public class MessageRedisRepository {

    private final RedisMessageListenerContainer redisMessageListener;

    private final RedisSubscriber redisSubscriber;

    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;

    private Map<Long, ChannelTopic> topics;

    @PostConstruct
    private void init(){
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public List<ChatRoom> findAllRoom(){
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public ChatRoom findRoomById(Long partyId){ // id는 대체 뭔가
        return opsHashChatRoom.get(CHAT_ROOMS, partyId);
    }

    public ChatRoom createChatRoom(Long partyId){
        ChatRoom chatRoom = ChatRoom.create(partyId);
        opsHashChatRoom.put(CHAT_ROOMS, String.valueOf(chatRoom.getPartyId()), chatRoom);
        return chatRoom;
    }

    public void enterChatRoom(Long partyId){
        ChannelTopic topic = topics.get(partyId);
        if(topic==null){
            topic = new ChannelTopic(String.valueOf(partyId));
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(partyId, topic);
        }
    }
    public ChannelTopic getTopic(Long partyId){
        if (topics.get(partyId)==null){
            enterChatRoom(partyId);
        }
        return topics.get(partyId);
    }

}
