package nbbang.com.nbbang.domain.party.repository;

import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.many.PartyFindRequestFilterDto;
import nbbang.com.nbbang.domain.party.dto.many.PartyListRequestFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManyPartyRepository extends JpaRepository<Party, Long>, ManyPartyRepositorySupport {

    Page<Party> findAll(Pageable pageable);

    @Override
    Page<Party> findAllParties(Pageable pageable, Boolean isMyParties, PartyListRequestFilterDto filter, Long cursorId, Long memberId, List<String> hashtags, Long ... partyId);

    @Override
    Page<Party> findAllByRequestDto(Pageable pageable, PartyFindRequestFilterDto requestFilterDto);

    Page<Party> findMyParties(Pageable pageable, PartyListRequestFilterDto filter, Long memberId);

    Party findTopByOrderByIdDesc();

    @Override
    Page<Party> findAllByCursoredFilterDto(Pageable pageable, PartyFindRequestFilterDto requestFilterDto, Long cursorId);
}
