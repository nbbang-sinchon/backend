package nbbang.com.nbbang.domain.chat.repository;

import nbbang.com.nbbang.domain.chat.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepositorySupport {
    Message findLastMessage(Long partyId);
    Page<Message> findAllByCursorId(Long partyId, Pageable pageable, Long cursorId);
    void bulkReadNumberPlus(Long lastReadId, Long partyId);
}
