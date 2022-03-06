package nbbang.com.nbbang.domain.party.service;

import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Service
@Transactional(readOnly = true)
public class PartySessionService {
    @Autowired PartyService partyService;
    @Autowired MemberService memberService;

    private ConcurrentHashMap<Long, String> findMap(Long partyId){
        Party party = partyService.findById(partyId);
        return party.getSessionMap();
    }
    public String findSession(Long partyId, Long memberId){
        ConcurrentHashMap<Long, String> sessionMap = new ConcurrentHashMap<>();
        if (!sessionMap.containsKey(memberId)){
            throw new IllegalArgumentException("요청한 멤버의 socket session이 저장되어 있지 않습니다.");
        }
        return sessionMap.get(memberId);
    }
    public Long findMemberId(Long partyId, String session){
        ConcurrentHashMap<Long, String> sessionMap = new ConcurrentHashMap<>();
        Set<Map.Entry<Long, String>> entries = sessionMap.entrySet();
        for (Map.Entry<Long, String> entry : entries) {
            if (entry.getValue().equals(session)) {
                return entry.getKey();
            }
        }
        throw new NotFoundException("요청한 socket session이 등록되어 있지 않습니다.");
    }


    public void addSession(Long partyId, Long memberId, String session){
        ConcurrentHashMap<Long, String> sessionMap = new ConcurrentHashMap<>();

        if (sessionMap.containsKey(memberId)){
            throw new IllegalArgumentException("요청한 멤버의 socket session이 이미 저장되어 있습니다.");
        }
        Member member = memberService.findById(memberId);
        member.updateActiveParty(partyId);
        sessionMap.put(memberId, session);
    }

    public void deleteSession(Long partyId, Long memberId){
        ConcurrentHashMap<Long, String> sessionMap = new ConcurrentHashMap<>();

        if (!sessionMap.containsKey(memberId)){
            throw new IllegalArgumentException("요청한 멤버의 socket session이 저장되어 있지 않습니다.");
        }
        Member member = memberService.findById(memberId);
        member.updateActiveParty(null);
        sessionMap.remove(memberId);
    }

}