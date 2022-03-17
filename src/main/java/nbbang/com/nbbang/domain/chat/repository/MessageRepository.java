package nbbang.com.nbbang.domain.chat.repository;

import nbbang.com.nbbang.domain.chat.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositorySupport {

    Page<Message> findAllByPartyIdAndIdGreaterThanEqualOrderByIdDesc(Long partyId, Long messageId, Pageable pageable);

    @Override
    Page<Message> findAllByCursorId(Long partyId, Long enterMessageId, Pageable pageable, Long cursorId);

    Integer countByPartyIdAndIdGreaterThan(Long partyId, Long id);

}

