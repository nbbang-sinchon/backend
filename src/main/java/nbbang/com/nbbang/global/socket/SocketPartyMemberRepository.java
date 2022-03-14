package nbbang.com.nbbang.global.socket;

public interface SocketPartyMemberRepository {

    void subscribe(Long partyId, Long memberId);
    void unsubscribe(Long partyId, Long memberId);
    Integer getActiveNumber(Long partyId);
    Boolean isActive(Long partyId, Long memberId);

}
