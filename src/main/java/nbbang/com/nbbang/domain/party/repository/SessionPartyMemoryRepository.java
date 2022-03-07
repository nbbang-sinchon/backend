package nbbang.com.nbbang.domain.party.repository;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.socket.MapUtil;
import nbbang.com.nbbang.global.socket.Session.SessionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Repository
@RequiredArgsConstructor
public class SessionPartyMemoryRepository implements SessionPartyRepository {

    private static ConcurrentMap<Long, List> sessionPartyMap = new ConcurrentHashMap<>();


    private final PartyService partyService;
    private final MemberService memberService;
    private final MapUtil mapUtil;


    private ConcurrentHashMap<String, Long> findMap(Long partyId){
        Party party = partyService.findById(partyId);
        return party.getSessionMap();
    }


    @Override
    public String findSession(Long partyId, Long memberId) {
        return null;
    }

    @Override
    public Long findMemberId(Long partyId, String session) {
        return null;
    }

    @Override
    public void addSession(Long partyId, String session, Long memberId) {

    }

    @Override
    public void deleteSession(Long partyId, Long memberId) {

    }

/*

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
*/


}
