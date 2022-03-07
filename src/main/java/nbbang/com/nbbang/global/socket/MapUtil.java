package nbbang.com.nbbang.global.socket;

import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class MapUtil {
    private static final String SESSION_NOT_FOUND = "요청한 socket session이 존재하지 않습니다.";
    private static final String SESSION_EXISTS = "요청한 socket session이 이미 저장되어 있습니다.";

    private static final String MEMBER_NOT_FOUND = "요청한 멤버의 socket session이 존재하지 않습니다.";
    private static final String MEMBER_EXISTS = "요청한 멤버가 이미 저장되어 있습니다.";

    public String findSession(ConcurrentMap map, Long memberId){
        if (!map.containsValue(memberId)){
            throw new IllegalArgumentException(MEMBER_NOT_FOUND);
        }
        return findKeyByValue(map, memberId);
    }

    public Long findMemberId(ConcurrentMap map, String session){
        if (!map.containsKey(session)){
            throw new IllegalArgumentException(SESSION_NOT_FOUND);
        }
        return (Long) map.get(session);
    }

    public void addSession(ConcurrentMap map, String session, Long memberId){
        if (map.containsKey(session)){
            throw new IllegalArgumentException(SESSION_EXISTS);
        }
        if (map.containsValue(memberId)){
            throw new IllegalArgumentException(MEMBER_EXISTS);
        }
        map.put(session, memberId);
    }


    public void deleteSession(ConcurrentMap map, String session){
        if (!map.containsKey(session)){
            throw new IllegalArgumentException(SESSION_NOT_FOUND);
        }
        map.remove(session);
    }

    public void deleteSession(ConcurrentMap map, Long memberId){
        if (!map.containsValue(memberId)){
            throw new IllegalArgumentException(SESSION_NOT_FOUND);
        }
        String session = findKeyByValue(map, memberId);
        map.remove(session);
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
