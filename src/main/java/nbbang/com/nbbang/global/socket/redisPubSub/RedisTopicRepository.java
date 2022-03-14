package nbbang.com.nbbang.global.socket.redisPubSub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.socket.redisPubSub.RedisSubscriber;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Repository
@Slf4j
public class RedisTopicRepository {

    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;

    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init(){
        topics = new HashMap<>();
    }

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
