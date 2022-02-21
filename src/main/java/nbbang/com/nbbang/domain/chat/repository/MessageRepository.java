package nbbang.com.nbbang.domain.chat.repository;

import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.party.domain.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositorySupport {

    Page<Message> findAllByPartyId(Long partyId, Pageable pageable);

    @Override
    Page<Message> findAllByCursorId(Long partyId, Pageable pageable, Long cursorId);

    Long countByPartyId(Long partyId);
}
