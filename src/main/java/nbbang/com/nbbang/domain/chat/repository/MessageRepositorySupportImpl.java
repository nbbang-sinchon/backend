package nbbang.com.nbbang.domain.chat.repository;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.QMessage;
import nbbang.com.nbbang.domain.party.domain.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class MessageRepositorySupportImpl implements MessageRepositorySupport {

    private final JPAQueryFactory query;

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
    public Page<Message> findAllByCursorId(Long partyId, Pageable pageable, Long cursorId) {
        QMessage message = QMessage.message;
        JPQLQuery<Message> q = query.selectFrom(message)
                .where(message.party.id.eq(partyId))
                .where(message.id.lt(cursorId))
                .orderBy(message.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        List<Message> res = q.fetch();
        Long count = query.selectFrom(message).stream().count();
        return new PageImpl<>(res, pageable, count);
    }
}
