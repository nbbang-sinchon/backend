package nbbang.com.nbbang.domain.party.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.SessionPartyMemoryRepository;
import nbbang.com.nbbang.global.socket.MapUtil;
import nbbang.com.nbbang.global.socket.Session.SessionMemberMemoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SessionPartyService {

    private final SessionPartyMemoryRepository sessionPartyMemoryRepository;
    private final MemberService memberService;

    public void addParty(Long partyId){
        sessionPartyMemoryRepository.addParty(partyId);
    }

    public Integer getActiveNumber(Long partyId){
        return sessionPartyMemoryRepository.getActiveNumber(partyId);
    }

    public String findSession(Long partyId, Long memberId){
        return sessionPartyMemoryRepository.findSession(partyId, memberId);
    }

    public Long findMemberId(Long partyId, String session){
        return sessionPartyMemoryRepository.findMemberId(partyId, session);
    }

    public void addSession(Long partyId, String session, Long memberId){
        sessionPartyMemoryRepository.addSession(partyId, session, memberId);
        memberService.findById(memberId).updateActiveParty(partyId);
    }
    public void deleteSession(Long partyId, Long memberId){
        sessionPartyMemoryRepository.deleteSession(partyId, memberId);
        memberService.findById(memberId).updateActiveParty(null);
    }

    public Long findPartyIdBySessionIfExists(String session) {
        return sessionPartyMemoryRepository.findPartyIdBySessionIfExists(session);
    }
}