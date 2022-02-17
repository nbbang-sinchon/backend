package nbbang.com.nbbang.domain.chat.repository;

import nbbang.com.nbbang.domain.chat.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRepositorySupport {
    Message findLastMessage(Long partyId);
    Page<Message> findAllByCursorId(Long partyId, Pageable pageable, Long cursorId);
}
