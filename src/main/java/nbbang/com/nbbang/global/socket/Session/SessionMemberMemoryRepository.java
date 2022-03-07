package nbbang.com.nbbang.global.socket.Session;

import nbbang.com.nbbang.global.socket.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.webjars.NotFoundException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class SessionMemberMemoryRepository implements SessionRepository {

    private static final String SESSION_NOT_FOUND = "요청한 socket session이 존재하지 않습니다.";
    private static final String SESSION_EXISTS = "요청한 socket session이 이미 저장되어 있습니다.";

    private static final String MEMBER_NOT_FOUND = "요청한 멤버의 socket session이 존재하지 않습니다.";
    private static final String MEMBER_EXISTS = "요청한 멤버가 이미 저장되어 있습니다.";


    private static ConcurrentMap<String, Long> memberSessionMap = new ConcurrentHashMap<>();

    @Override
    public String findSession(Long memberId){
        if (!memberSessionMap.containsValue(memberId)){
            throw new IllegalArgumentException(MEMBER_NOT_FOUND);
        }
        return findKeyByValue(memberSessionMap, memberId);
    }

    @Override
    public Long findMemberId(String session){
        if (!memberSessionMap.containsKey(session)){
            throw new IllegalArgumentException(SESSION_NOT_FOUND);
        }
        return memberSessionMap.get(session);
    }

    @Override
    public void addSession(String session, Long memberId){
        if (memberSessionMap.containsKey(session)){
            throw new IllegalArgumentException(SESSION_EXISTS);
        }
        if (memberSessionMap.containsValue(memberId)){
            throw new IllegalArgumentException(MEMBER_EXISTS);
        }
        memberSessionMap.put(session, memberId);
    }

    @Override
    public void deleteSession(Long memberId){
        if (!memberSessionMap.containsValue(memberId)){
            throw new IllegalArgumentException(SESSION_NOT_FOUND);
        }
        String session = findKeyByValue(memberSessionMap, memberId);
        memberSessionMap.remove(session);
    }

    @Override
    public void deleteSession(String session){
        if (!memberSessionMap.containsKey(session)){
            throw new IllegalArgumentException(SESSION_NOT_FOUND);
        }
        memberSessionMap.remove(session);
    }

    private String findKeyByValue(ConcurrentMap map, Long memberId){
        Set<Map.Entry<String, Long>> entries = map.entrySet();
        for (Map.Entry<String, Long> entry : entries) {
            if (entry.getValue().equals(memberId)) {
                return entry.getKey();
            }
        }
        throw new NotFoundException(MEMBER_NOT_FOUND);
    }

}
