package nbbang.com.nbbang.domain.partymember.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static nbbang.com.nbbang.domain.partymember.domain.QPartyMember.*;
import static nbbang.com.nbbang.domain.chat.domain.QMessage.message;


@RequiredArgsConstructor
public class PartyMemberRepositorySupportImpl implements PartyMemberRepositorySupport {
    private final JPAQueryFactory query;

    @Override
    public Boolean isThereNotReadMessageByMemberId(Long memberId) {
        int size = query.selectFrom(partyMember)
                .where(partyMember.member.id.eq(memberId))
                .where(JPAExpressions
                        .selectFrom(message)
                        .where(message.party.eq(partyMember.party))
                        .where(message.id.gt(partyMember.lastReadMessage.id)).exists())
                .fetch().size();
        return size>0;
    }

    @Override
    public void updateLastReadMessage(Long partyId, Long memberId) {
        query.update(partyMember)
                .set(partyMember.lastReadMessage.id, JPAExpressions.select(message.id.max()).from(message)
                        .where(message.party.id.eq(partyId)))
                .where(partyMember.member.id.eq(memberId))
                .where(partyMember.party.id.eq(partyId))
                .execute();

    }
}
