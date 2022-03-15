package nbbang.com.nbbang.domain.bbangpan.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.bbangpan.domain.QPartyMember;

import static nbbang.com.nbbang.domain.chat.domain.QMessage.message;


@RequiredArgsConstructor
public class PartyMemberRepositorySupportImpl implements PartyMemberRepositorySupport{
    private final JPAQueryFactory query;
    @Override
    public Boolean isThereNotReadMessageByMemberId(Long memberId) {
        QPartyMember partyMember = QPartyMember.partyMember;
        int size = query.selectFrom(partyMember)
                .where(partyMember.member.id.eq(memberId))
                .where(JPAExpressions
                        .selectFrom(message)
                        .where(message.party.eq(partyMember.party))
                        .where(message.id.gt(partyMember.lastReadMessage.id)).exists())
                .fetch().size();
        return size>0;
    }
}
