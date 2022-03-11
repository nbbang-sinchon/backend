package nbbang.com.nbbang.global.socket;

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
import static nbbang.com.nbbang.global.socket.SocketDestination.*;


@RequiredArgsConstructor
@Repository
@Slf4j
public class MessageRedisRepository {

    private final RedisMessageListenerContainer redisMessageListener;

    private final RedisSubscriber redisSubscriber;

    private final RedisTemplate<String, Object> redisTemplate;
    //private HashOperations<String, String, ChatRoom> opsHashChatRoom;

    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init(){
        //opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

/*
    public List<ChatRoom> findAllRoom(){
        return opsHashChatRoom.values(CHAT_ROOM);
    }

    public ChatRoom findRoomById(Long partyId){ // id는 대체 뭔가
        return opsHashChatRoom.get(CHAT_ROOM, partyId);
    }

    public ChatRoom createChatRoom(Long partyId){
        ChatRoom chatRoom = ChatRoom.create(partyId);
        opsHashChatRoom.put(CHAT_ROOM, String.valueOf(chatRoom.getPartyId()), chatRoom);
        return chatRoom;
    }
*/

    public void addChatRoom(String topicName){
        ChannelTopic topic = topics.get(topicName);
        if(topic==null){
            topic = new ChannelTopic(topicName);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(topicName, topic);
        }
    }

    public ChannelTopic getTopic(String topicName){
        if (topics.get(topicName)==null){
            addChatRoom(topicName);
        }
        return topics.get(topicName);
    }

}
