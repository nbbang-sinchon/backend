package nbbang.com.nbbang.global.socket.repository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.socket.PartyMemberPair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

//@Repository
@RequiredArgsConstructor
public class MemorySocketPartyMemberRepository implements SocketPartyMemberRepository{
    private static ConcurrentMap<PartyMemberPair, Integer> sessionPartyMap = new ConcurrentHashMap<>();

    @Override
    public void updateActiveNumber(PartyMemberPair pair, Integer cnt) {
        sessionPartyMap.putIfAbsent(pair, 0);
        sessionPartyMap.put(pair, sessionPartyMap.get(pair) + cnt);
    }

    @Override
    public Map getAllEntries() {
        return sessionPartyMap;
    }

    @Override
    public Boolean hasKey(PartyMemberPair pair) {
        return sessionPartyMap.containsKey(pair);
    }

    @Override
    public Integer getPartyMemberPairActiveNumber(PartyMemberPair pair) {
        return sessionPartyMap.get(pair);
    }

}
