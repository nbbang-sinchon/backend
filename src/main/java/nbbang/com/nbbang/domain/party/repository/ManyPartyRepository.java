package nbbang.com.nbbang.domain.party.repository;

import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestDto;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestFilterDto;
import nbbang.com.nbbang.domain.party.dto.PartyListRequestFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ManyPartyRepository extends JpaRepository<Party, Long>, ManyPartyRepositorySupport {

    Page<Party> findAll(Pageable pageable);

    @Override
    Page<Party> findAllByRequestDto(Pageable pageable, PartyFindRequestFilterDto requestFilterDto);

    Page<Party> findMyParties(Pageable pageable, PartyListRequestFilterDto filter, Long memberId);

    Party findTopByOrderByIdDesc();

    @Override
    Page<Party> findAllByCursoredFilterDto(Pageable pageable, PartyFindRequestFilterDto requestFilterDto, Long cursorId);
}
