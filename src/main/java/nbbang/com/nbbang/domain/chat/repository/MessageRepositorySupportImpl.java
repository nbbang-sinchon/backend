package nbbang.com.nbbang.domain.chat.repository;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.MessageType;
import nbbang.com.nbbang.domain.chat.domain.QMessage;
import nbbang.com.nbbang.domain.party.domain.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class MessageRepositorySupportImpl implements MessageRepositorySupport {

    private final JPAQueryFactory query;
    private final EntityManager em;

    @Override
    public Message findLastMessage(Long partyId) {
        QMessage message = QMessage.message;
        JPQLQuery<Message> q = query.selectFrom(message)
                .where(message.party.id.eq(partyId))
                .orderBy(message.id.desc())
                .limit(1);
        Message res = q.fetchOne();
        if (res == null) {
            return null;
        }
        return res;
    }

    @Override
    public Page<Message> findAllByCursorId(Long partyId, Long enterMessageId, Pageable pageable, Long cursorId) {
        QMessage message = QMessage.message;
        JPQLQuery<Message> q = query.selectFrom(message)
                .where(message.party.id.eq(partyId))
                .where(message.id.lt(cursorId))
                .where(message.id.goe(enterMessageId))
                .orderBy(message.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        List<Message> res = q.fetch();
        Long count = query.selectFrom(message).stream().count();
        return new PageImpl<>(res, pageable, count);
    }

    @Override
    public void bulkNotReadMinusPlus(Long lastReadId, Long partyId) {
        QMessage message = QMessage.message;
                query
                .update(message)
                .set(message.notReadNumber,message.notReadNumber.subtract(1))
                .where(message.id.gt(lastReadId))
                .where(message.party.id.eq(partyId))
                .execute();
        em.flush();
        em.clear();
        log.info("[Subtract] Not Read Number -1 gt{}", lastReadId);
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
