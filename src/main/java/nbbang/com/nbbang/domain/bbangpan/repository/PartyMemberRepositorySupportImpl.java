package nbbang.com.nbbang.domain.bbangpan.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.domain.QPartyMember;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.QMessage;
import nbbang.com.nbbang.domain.party.domain.QParty;

import static nbbang.com.nbbang.domain.bbangpan.domain.QPartyMember.*;
import static nbbang.com.nbbang.domain.chat.domain.QMessage.message;
import static nbbang.com.nbbang.domain.party.domain.QParty.*;


@RequiredArgsConstructor
public class PartyMemberRepositorySupportImpl implements PartyMemberRepositorySupport{
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
                .where(partyMember.member.id.eq(memberId))
                .where(partyMember.party.id.eq(partyId))
                .set(partyMember.lastReadMessage, JPAExpressions.selectFrom(message)
                        .where(message.party.id.eq(partyId))
                        .orderBy(message.id.desc())
                        .fetchOne())
                .execute();
    }
}
