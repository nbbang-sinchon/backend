package nbbang.com.nbbang.global.socket;


import com.mysema.commons.lang.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SessionPartyGlobalRepository {

    private static ConcurrentMap<Pair<Long, Long>, Integer> sessionPartyMap = new ConcurrentHashMap<>();

    public void subscribe(Long partyId, Long memberId){
        Pair<Long, Long> pair = new Pair<>(partyId, memberId);
        if (sessionPartyMap.containsKey(pair)){
            sessionPartyMap.put(pair, sessionPartyMap.get(pair) + 1);
        }else{
            sessionPartyMap.put(pair, 1);
        }
    }

    public void unsubscribe(Long partyId, Long memberId){
        Pair<Long, Long> pair = new Pair<>(partyId, memberId);
        if ((!sessionPartyMap.containsKey(pair)) || (sessionPartyMap.get(pair) < 1)){
            throw new IllegalArgumentException("[UNSUBSCRIBE] partyId, memberId에 해당하는 소켓 연결을 찾을 수 없습니다. ");
        }
        sessionPartyMap.put(pair, sessionPartyMap.get(pair) - 1);
    }

    public Integer getActiveNumber(Long partyId) {
        long count = sessionPartyMap.entrySet().stream().filter(entry -> entry.getKey().getFirst().equals(partyId) && entry.getValue() > 0).count();
        return Integer.valueOf((int) count);
    }

    public Boolean isActive(Long partyId, Long memberId) {
        Pair<Long, Long> pair = new Pair<>(partyId, memberId);
        if(sessionPartyMap.containsKey(pair) &&  (sessionPartyMap.get(pair) > 0)){
            return true;
        }
        return false;
    }

}
