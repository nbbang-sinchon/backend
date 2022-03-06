package nbbang.com.nbbang.global.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SessionMemberService {
    @Autowired private MapUtil mapUtil;

    private static ConcurrentHashMap<String, Long> memberSessionMap = new ConcurrentHashMap<>();

    public String findSession(Long memberId){
        return mapUtil.findSession(memberSessionMap, memberId);
    }

    public Long findMemberId(String session){
        return mapUtil.findMemberId(memberSessionMap, session);
    }

    public void addSession(String session, Long memberId){
        mapUtil.addSession(memberSessionMap, session, memberId);
    }
    public void deleteSession(Long memberId){
        mapUtil.deleteSession(memberSessionMap,memberId);
    }

    public void deleteSession(String session){
        mapUtil.deleteSession(memberSessionMap,session);
    }

}
