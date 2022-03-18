package nbbang.com.nbbang.global.socket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.socket.PartyMemberPair;
import nbbang.com.nbbang.global.socket.repository.SocketPartyMemberRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketPartyMemberService {

    private final SocketPartyMemberRepository socketPartyMemberRepository;

    public void subscribe(Long partyId, Long memberId) {
        PartyMemberPair pair = PartyMemberPair.create(partyId, memberId);
        socketPartyMemberRepository.updateActiveNumber(pair, 1);
    }

    public void unsubscribe(Long partyId, Long memberId) {
        PartyMemberPair pair = PartyMemberPair.create(partyId, memberId);

        if (!socketPartyMemberRepository.hasKey(pair) || socketPartyMemberRepository.getPartyMemberPairActiveNumber(pair) < 1){
            throw new IllegalArgumentException("[UNSUBSCRIBE ERROR] partyId, memberId에 해당하는 소켓 연결을 찾을 수 없습니다.");
        }
        socketPartyMemberRepository.updateActiveNumber(pair, -1);
    }

    public Integer getPartyActiveNumber(Long partyId) {
        Map<PartyMemberPair, Integer> entries = socketPartyMemberRepository.getAllEntries();
        long count = entries.entrySet().stream().filter(entry -> entry.getKey().getPartyId().equals(partyId) && entry.getValue() > 0).count();
        return Integer.valueOf((int) count);
    }

    public Integer getPartyMemberActiveNumber(Long partyId, Long memberId){
        PartyMemberPair pair = PartyMemberPair.create(partyId, memberId);
        return socketPartyMemberRepository.getPartyMemberPairActiveNumber(pair);
    }

    public Boolean isActive(Long partyId, Long memberId) {
        PartyMemberPair pair = PartyMemberPair.create(partyId, memberId);
        if(socketPartyMemberRepository.hasKey(pair) &&  socketPartyMemberRepository.getPartyMemberPairActiveNumber(pair) > 0){
            return true;
        }
        return false;
    }
}
