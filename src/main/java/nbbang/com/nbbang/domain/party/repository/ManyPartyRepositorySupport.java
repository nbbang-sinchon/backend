package nbbang.com.nbbang.domain.party.repository;

import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestDto;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestFilterDto;
import nbbang.com.nbbang.domain.party.dto.PartyListRequestFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManyPartyRepositorySupport {

    Page<Party> findAllByRequestDto(Pageable pageable, PartyFindRequestFilterDto requestFilterDto);

    Page<Party> findMyParties(Pageable pageable, PartyListRequestFilterDto filter, Long memberId);

    Page<Party> findAllByCursoredFilterDto(Pageable pageable, PartyFindRequestFilterDto requestFilterDto, Long cursorId);

    Page<Party> findAllParties(Pageable pageable, PartyListRequestFilterDto filter, Long cursorId, Long memberId, Long ... partyId);

}
