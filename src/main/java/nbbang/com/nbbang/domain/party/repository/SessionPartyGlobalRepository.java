package nbbang.com.nbbang.domain.party.repository;


import com.mysema.commons.lang.Pair;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.socket.MapUtil;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
@RequiredArgsConstructor
public class SessionPartyGlobalRepository {

    private final MapUtil mapUtil;

    private static ConcurrentMap<Pair<Long, Long>, Integer> sessionPartyMap = new ConcurrentHashMap<>();
    private static final String PARTY_EXISTS = "요청한 파티가 이미 저장되어 있습니다.";

    public void subscribe(Long memberId, Long partyId){
        Pair<Long, Long> pair = new Pair<>(memberId, partyId);
        if (sessionPartyMap.containsKey(pair)){
            sessionPartyMap.put(pair, sessionPartyMap.get(pair) + 1);
        }else{
            sessionPartyMap.put(pair, 1);
        }
    }

    public void unsubscribe(Long memberId, Long partyId){
        Pair<Long, Long> pair = new Pair<>(memberId, partyId);
        if ((!sessionPartyMap.containsKey(pair)) || (sessionPartyMap.get(pair) < 1)){
            throw new IllegalArgumentException("memberId, partyId와 맞는 데이터가 존재하지 않습니다.");
        }
        sessionPartyMap.put(pair, sessionPartyMap.get(pair) - 1);
    }

    public Integer getActiveNumber(Long partyId) {
        Integer cnt = 0;
        long count = sessionPartyMap.entrySet().stream().filter(entry -> entry.getKey().getSecond().equals(partyId) && entry.getValue() > 0).count();
        return Integer.valueOf((int) count);
    }


}
