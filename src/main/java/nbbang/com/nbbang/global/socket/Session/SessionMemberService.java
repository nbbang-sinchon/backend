package nbbang.com.nbbang.global.socket.Session;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.socket.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionMemberService {
    private final SessionMemberMemoryRepository sessionMemberMemoryRepository;

    public String findSession(Long memberId){
        return sessionMemberMemoryRepository.findSession(memberId);
    }

    public Long findMemberId(String session){
        return sessionMemberMemoryRepository.findMemberId(session);
    }

    public void addSession(String session, Long memberId){
        sessionMemberMemoryRepository.addSession(session, memberId);
    }
    public void deleteSession(Long memberId){
        sessionMemberMemoryRepository.deleteSession(memberId);
    }

    public void deleteSession(String session){
        sessionMemberMemoryRepository.deleteSession(session);
    }

}
