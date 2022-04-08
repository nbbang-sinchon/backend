package nbbang.com.nbbang.domain.bbangpan.repository;

public interface PartyMemberRepositorySupport {
    Boolean isThereNotReadMessageByMemberId(Long memberId);

    void updateLastReadMessage(Long partyId, Long memberId);
}
