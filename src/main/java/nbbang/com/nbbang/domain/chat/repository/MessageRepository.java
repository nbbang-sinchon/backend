package nbbang.com.nbbang.domain.chat.repository;

import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.domain.MessageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositorySupport {

    Page<Message> findAllByPartyIdOrderByIdDesc(Long partyId, Pageable pageable);

    @Override
    Page<Message> findAllByCursorId(Long partyId, Pageable pageable, Long cursorId);

    Long countByPartyId(Long partyId);

    Integer countByPartyIdAndIdGreaterThan(Long partyId, Long id);

    Message findByTypeAndPartyIdAndSenderId(MessageType messageType, Long partyId, Long memberId);
    // And
    // findFistByOrderByIdDesc
}

