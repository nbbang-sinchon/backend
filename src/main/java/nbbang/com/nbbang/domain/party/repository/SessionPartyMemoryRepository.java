package nbbang.com.nbbang.domain.party.repository;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.global.socket.MapUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Repository
@RequiredArgsConstructor
public class SessionPartyMemoryRepository implements SessionPartyRepository {

    private final MapUtil mapUtil;

    private static ConcurrentMap<Long, ConcurrentMap<String, Long>> sessionPartyMap = new ConcurrentHashMap<>();
    private static final String PARTY_EXISTS = "요청한 파티가 이미 저장되어 있습니다.";

    public void addParty(Long partyId) {
        if(sessionPartyMap.containsKey(partyId)){
            throw new IllegalArgumentException(PARTY_EXISTS);
        }
        sessionPartyMap.put(partyId, new ConcurrentHashMap<>());
    }

    public ConcurrentMap<String, Long> findMap(Long partyId) {
        if(!sessionPartyMap.containsKey(partyId)){
            addParty(partyId);
        }
        ConcurrentMap<String, Long> map = sessionPartyMap.get(partyId);
        return map;
    }


    @Override
    public String findSession(Long partyId, Long memberId) {
        ConcurrentMap<String, Long> partySessionMap = findMap(partyId);
        return mapUtil.findSession(partySessionMap, memberId);
    }

    @Override
    public Long findMemberId(Long partyId, String session) {
        ConcurrentMap<String, Long> partySessionMap = findMap(partyId);
        return mapUtil.findMemberId(partySessionMap, session);
    }

    @Override
    public void addSession(Long partyId, String session, Long memberId) {
        ConcurrentMap<String, Long> partySessionMap = findMap(partyId);
        mapUtil.addSession(partySessionMap, session, memberId);
    }

    @Override
    public void deleteSession(Long partyId, Long memberId) {
        ConcurrentMap<String, Long> partySessionMap = findMap(partyId);
        mapUtil.deleteSession(partySessionMap, memberId);
    }

    @Override
    public Integer getActiveNumber(Long partyId) {
        System.out.println("sessionPartyMap = " + sessionPartyMap);
        ConcurrentMap<String, Long> partySessionMap = findMap(partyId);
        return partySessionMap.size();
    }

    public Long findPartyIdBySessionIfExists(String session) {
        for (Map.Entry<Long, ConcurrentMap<String, Long>> mapEntry : sessionPartyMap.entrySet()) {
            if(mapEntry.getValue().containsKey(session)){
                return mapEntry.getKey();
            }
        }
        return null;
    }
}
