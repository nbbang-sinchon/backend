package nbbang.com.nbbang.domain.party.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.global.socket.MapUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionPartyService {
    private final PartyService partyService;
    private final MemberService memberService;
    private final MapUtil mapUtil;

    private ConcurrentHashMap<String, Long> findMap(Long partyId){
        Party party = partyService.findById(partyId);
        System.out.println("party.getSessionMap() = " + party.getSessionMap());
        return party.getSessionMap();
    }

    public String findSession(Long partyId, Long memberId){
        ConcurrentHashMap<String, Long> sessionMap = findMap(partyId);
        return mapUtil.findSession(sessionMap, memberId);
    }

    public Long findMemberId(Long partyId, String session){
        ConcurrentHashMap<String, Long> sessionMap = findMap(partyId);
        return mapUtil.findMemberId(sessionMap, session);
    }

    public void addSession(Long partyId, String session, Long memberId){
        ConcurrentHashMap<String, Long> sessionMap = findMap(partyId);
        mapUtil.addSession(sessionMap, session, memberId);
        Member member = memberService.findById(memberId);
        member.updateActiveParty(partyId);
        System.out.println("sessionMap = " + sessionMap);
    }

    public void deleteSession(Long partyId, Long memberId){
        ConcurrentHashMap<String, Long> sessionMap = findMap(partyId);
        mapUtil.deleteSession(sessionMap,memberId);
        Member member = memberService.findById(memberId);
        member.updateActiveParty(null);
    }
}