package nbbang.com.nbbang.global.socket.repository;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.socket.PartyMemberPair;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RedisSocketPartyMemberRepository implements SocketPartyMemberRepository{

    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, PartyMemberPair, Integer> opsHashChatRoom;

    private static final String CHAT_ROOM = "CHAT_ROOM";

    @PostConstruct
    private void init(){
        opsHashChatRoom = redisTemplate.opsForHash();
    }


    @Override
    public void updateActiveNumber(PartyMemberPair pair, Integer cnt) {
        opsHashChatRoom.putIfAbsent(CHAT_ROOM, pair, 0);
        opsHashChatRoom.put(CHAT_ROOM, pair, opsHashChatRoom.get(CHAT_ROOM, pair) + cnt);
    }

    @Override
    public Map getAllEntries(){
        Map<PartyMemberPair, Integer> entries = opsHashChatRoom.entries(CHAT_ROOM);
        return entries;
    }

    @Override
    public Boolean hasKey(PartyMemberPair pair){
        return opsHashChatRoom.hasKey(CHAT_ROOM, pair);
    }

    @Override
    public Integer getPartyMemberPairActiveNumber(PartyMemberPair pair){
        if(!hasKey(pair)){return 0;}
        return opsHashChatRoom.get(CHAT_ROOM, pair);
    }


    @PreDestroy
    private void deleteAllData(){
        redisTemplate.delete(CHAT_ROOM);
    }

}
