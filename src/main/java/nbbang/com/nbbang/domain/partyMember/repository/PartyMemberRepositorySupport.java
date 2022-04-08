package nbbang.com.nbbang.domain.partyMember.repository;

public interface PartyMemberRepositorySupport {
    Boolean isThereNotReadMessageByMemberId(Long memberId);

    void updateLastReadMessage(Long partyId, Long memberId);
}
