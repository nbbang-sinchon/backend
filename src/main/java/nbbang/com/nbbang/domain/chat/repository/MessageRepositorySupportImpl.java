package nbbang.com.nbbang.domain.chat.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.MessageType;
import nbbang.com.nbbang.domain.chat.domain.QMessage;
import nbbang.com.nbbang.domain.member.domain.QMember;
import nbbang.com.nbbang.domain.party.domain.QParty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static nbbang.com.nbbang.domain.partymember.domain.QPartyMember.*;
import static nbbang.com.nbbang.domain.chat.domain.QMessage.*;

@RequiredArgsConstructor
@Slf4j
public class MessageRepositorySupportImpl implements MessageRepositorySupport {

    private final JPAQueryFactory query;
    private final EntityManager em;

    @Override
    public Message findLastMessage(Long partyId) {
        QMessage message = QMessage.message;
        return query.selectFrom(message)
                .where(message.party.id.eq(partyId))
                .orderBy(message.id.desc()).limit(1)
                .fetchOne();
    }

    @Override
    public Page<Message> findAllByCursorId(Long partyId, Long enterMessageId, Pageable pageable, Long cursorId) {
        QMessage message = QMessage.message;
        QParty party = QParty.party;
        QMember member = QMember.member;
        JPQLQuery<Message> q = query.selectFrom(message)
                .leftJoin(message.party, party)
                .fetchJoin()
                .leftJoin(message.sender, member)
                .fetchJoin()
                .where(message.party.id.eq(partyId))
                .where(message.id.goe(enterMessageId))
                .orderBy(message.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        if (cursorId != null) {
            q.where(message.id.lt(cursorId));
        }
        List<Message> res = q.fetch();
        //Long count = query.selectFrom(message).stream().count();
        return new PageImpl<>(res, pageable, 0L);
    }

    @Override
    public void bulkNotReadSubtract(Long partyId, Long memberId) {
        query
                .update(message)
                .set(message.notReadNumber,message.notReadNumber.subtract(1))
                .where(message.id.gt(
                        JPAExpressions
                                .select(partyMember.lastReadMessage.id)
                                .from(partyMember)
                                .where(partyMember.member.id.eq(memberId))
                                .where(partyMember.party.id.eq(partyId))
                ))
                .where(message.party.id.eq(partyId))
                .execute();
        em.flush();
        em.clear();

    }

    @Override
    public Message findFirstByTypeAndPartyIdAndSenderId(MessageType messageType, Long partyId, Long memberId) {
        QMessage message = QMessage.message;
        Message m = query
                .selectFrom(message)
                .where(message.type.eq(messageType))
                .where(message.party.id.eq(partyId))
                .where(message.sender.id.eq(memberId))
                .orderBy(message.id.desc())
                .fetchFirst();
        return m;
    }
}
