package nbbang.com.nbbang.domain.party.repository;

public interface SessionPartyRepository {
    String findSession(Long partyId, Long memberId);
    Long findMemberId(Long partyId, String session);
    void addSession(Long partyId, String session, Long memberId);
    void deleteSession(Long partyId, Long memberId);
}
