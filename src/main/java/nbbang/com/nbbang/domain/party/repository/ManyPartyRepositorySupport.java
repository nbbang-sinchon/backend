package nbbang.com.nbbang.domain.party.repository;

import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.many.PartyFindRequestFilterDto;
import nbbang.com.nbbang.domain.party.dto.many.PartyListRequestFilterDto;
import nbbang.com.nbbang.domain.party.repository.v2.PartyListDtoV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ManyPartyRepositorySupport {

    Page<Party> findAllByRequestDto(Pageable pageable, PartyFindRequestFilterDto requestFilterDto);

    Page<Party> findMyParties(Pageable pageable, PartyListRequestFilterDto filter, Long memberId);

    Page<PartyListDtoV2> findAllPartiesV2(Pageable pageable, PartyListRequestFilterDto filter, Long cursorId, Long memberId);

    Page<Party> findAllByCursoredFilterDto(Pageable pageable, PartyFindRequestFilterDto requestFilterDto, Long cursorId);

    Page<Party> findAllParties(Pageable pageable, Boolean isMyParties, PartyListRequestFilterDto filter, Long cursorId, Long memberId, List<String> hashtags, Long ... partyId);

}
