package nbbang.com.nbbang.global.socket;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MemberSessionService {
    private static ConcurrentHashMap<Long, String> memberSessionMap = new ConcurrentHashMap<>();

    public String findSession(Long memberId){
        if (!memberSessionMap.containsKey(memberId)){
            throw new IllegalArgumentException("요청한 멤버의 socket session이 저장되어 있지 않습니다.");
        }
        return memberSessionMap.get(memberId);
    }
    public Long findMemberId(String session){
        Set<Map.Entry<Long, String>> entries = memberSessionMap.entrySet();
        for (Map.Entry<Long, String> entry : entries) {
            if (entry.getValue().equals(session)) {
                return entry.getKey();
            }
        }
        throw new NotFoundException("요청한 socket session이 등록되어 있지 않습니다.");
    }


    public void addSession(Long memberId, String session){
        if (memberSessionMap.containsKey(memberId)){
            throw new IllegalArgumentException("요청한 멤버의 socket session이 이미 저장되어 있습니다.");
        }
        memberSessionMap.put(memberId, session);
    }
    public void deleteSession(Long memberId){
        if (!memberSessionMap.containsKey(memberId)){
            throw new IllegalArgumentException("요청한 멤버의 socket session이 저장되어 있지 않습니다.");
        }
        memberSessionMap.remove(memberId);
    }

}
