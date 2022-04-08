package nbbang.com.nbbang.domain.partymember.repository;

public interface PartyMemberRepositorySupport {
    Boolean isThereNotReadMessageByMemberId(Long memberId);

    void updateLastReadMessage(Long partyId, Long memberId);
}
