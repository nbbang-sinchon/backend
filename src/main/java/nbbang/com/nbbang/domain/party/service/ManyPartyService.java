package nbbang.com.nbbang.domain.party.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestDto;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestFilterDto;
import nbbang.com.nbbang.domain.party.dto.PartyListRequestFilterDto;
import nbbang.com.nbbang.domain.party.repository.ManyPartyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class ManyPartyService {
    private final ManyPartyRepository manyPartyRepository;


    public Page<Party> findAllParties(Pageable pageable, PartyListRequestFilterDto filter, Long cursorId, Long memberId, Long ... partyId) {
        return manyPartyRepository.findAllParties(pageable, filter, cursorId, memberId, partyId);
    }

    public Page<Party> findAllByRequestDto(Pageable pageable, PartyFindRequestFilterDto requestFilterDto) {
        return manyPartyRepository.findAllByRequestDto(pageable, requestFilterDto);
    }

    public Page<Party> findAllByCursoredFilterDto(Pageable pageable, PartyFindRequestFilterDto requestFilterDto, Long cursorId) {
        return manyPartyRepository.findAllByCursoredFilterDto(pageable, requestFilterDto, cursorId);
    }

    public Page<Party> findMyParties(Pageable pageable, PartyListRequestFilterDto filter, Long memberId) {
        return manyPartyRepository.findMyParties(pageable, filter, memberId);
    }

    public Party findLastParty() {
        return manyPartyRepository.findTopByOrderByIdDesc();
    }
}
