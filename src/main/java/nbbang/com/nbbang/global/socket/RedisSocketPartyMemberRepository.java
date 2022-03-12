package nbbang.com.nbbang.global.socket;

import com.mysema.commons.lang.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Repository
@RequiredArgsConstructor
public class RedisSocketPartyMemberRepository implements SocketPartyMemberRepository{

    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String,PartyMemberPair, Integer> opsHashChatRoom;

    private static final String CHAT_ROOM = "CHAT_ROOM";

    @PostConstruct
    private void init(){
        opsHashChatRoom = redisTemplate.opsForHash();
    }


    @Override
    public void subscribe(Long partyId, Long memberId){
        PartyMemberPair pair = PartyMemberPair.create(partyId, memberId);
        opsHashChatRoom.putIfAbsent(CHAT_ROOM, pair, 0);
        opsHashChatRoom.put(CHAT_ROOM, pair, opsHashChatRoom.get(CHAT_ROOM, pair) + 1);
    }

    @Override
    public void unsubscribe(Long partyId, Long memberId){
        PartyMemberPair pair = PartyMemberPair.create(partyId, memberId);
        if ((!opsHashChatRoom.hasKey(CHAT_ROOM, pair)) || (opsHashChatRoom.get(CHAT_ROOM, pair) < 1)){
            throw new IllegalArgumentException("[UNSUBSCRIBE ERROR] partyId, memberId에 해당하는 소켓 연결을 찾을 수 없습니다. ");
        }
        opsHashChatRoom.put(CHAT_ROOM, pair, opsHashChatRoom.get(CHAT_ROOM, pair) - 1);
    }

    @Override
    public Integer getActiveNumber(Long partyId) {
        long count = opsHashChatRoom.entries(CHAT_ROOM).entrySet().stream().filter(entry -> entry.getKey().getPartyId().equals(partyId) && entry.getValue() > 0).count();
        return Integer.valueOf((int) count);
    }

    @Override
    public Boolean isActive(Long partyId, Long memberId) {
        PartyMemberPair pair = PartyMemberPair.create(partyId, memberId);
        if(opsHashChatRoom.hasKey(CHAT_ROOM, pair) &&  (opsHashChatRoom.get(CHAT_ROOM, pair) > 0)){
            return true;
        }
        return false;
    }

    @PreDestroy
    private void deleteAllData(){
        redisTemplate.delete(CHAT_ROOM);
    }
}
