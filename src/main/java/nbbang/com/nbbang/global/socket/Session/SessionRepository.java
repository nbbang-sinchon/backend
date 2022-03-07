package nbbang.com.nbbang.global.socket.Session;

public interface SessionRepository {
    String findSession(Long memberId);
    Long findMemberId(String session);
    void addSession(String session, Long memberId);
    void deleteSession(Long memberId);
    void deleteSession(String session);
}
