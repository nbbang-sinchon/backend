package nbbang.com.nbbang.global.socket.repository;

import nbbang.com.nbbang.global.socket.PartyMemberPair;

import java.util.Map;

public interface SocketPartyMemberRepository {

    void updateActiveNumber(PartyMemberPair pair, Integer cnt);
    Map getAllEntries();
    Boolean hasKey(PartyMemberPair pair);
    Integer getPartyMemberPairActiveNumber(PartyMemberPair pair);
}
