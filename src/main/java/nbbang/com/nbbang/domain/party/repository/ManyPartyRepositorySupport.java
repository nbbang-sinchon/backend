package nbbang.com.nbbang.domain.party.repository;

import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.many.PartyListRequestFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManyPartyRepositorySupport {

    Page<Party> findAllParties(Pageable pageable, PartyListRequestFilterDto filter, Long cursorId, Long memberId);

}
