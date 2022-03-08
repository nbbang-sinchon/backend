package nbbang.com.nbbang.domain.party.repository;


import com.mysema.commons.lang.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.socket.MapUtil;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SessionPartyGlobalRepository {

    private final MapUtil mapUtil;

    private static ConcurrentMap<Pair<Long, Long>, Integer> sessionPartyMap = new ConcurrentHashMap<>();
    private static final String PARTY_EXISTS = "요청한 파티가 이미 저장되어 있습니다.";

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
            throw new IllegalArgumentException("memberId, partyId와 맞는 데이터가 존재하지 않습니다.");
        }
        sessionPartyMap.put(pair, sessionPartyMap.get(pair) - 1);
    }

    public Integer getActiveNumber(Long partyId) {
        long count = sessionPartyMap.entrySet().stream().filter(entry -> entry.getKey().getFirst().equals(partyId) && entry.getValue() > 0).count();
        return Integer.valueOf((int) count);
    }


}
