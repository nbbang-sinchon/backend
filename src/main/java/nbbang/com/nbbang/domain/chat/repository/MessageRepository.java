package nbbang.com.nbbang.domain.chat.repository;

import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.party.domain.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositorySupport {

    Page<Message> findAllByPartyIdOrderByIdDesc(Long partyId, Pageable pageable);

    @Override
    Page<Message> findAllByCursorId(Long partyId, Pageable pageable, Long cursorId);

    Long countByPartyId(Long partyId);

    @Modifying(clearAutomatically = true)
    @Query(value="update Message m set m.read_number = m.read_number + 1 " +
            "where m.message_id > :lastReadId and m.party_id = :partyId", nativeQuery=true)
    int bulkReadNumberPlus(@Param("lastReadId") Long lastReadId, @Param("partyId") Long partyId);

}
