package nbbang.com.nbbang.domain.party.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestDto;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestFilterDto;
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

    public Page<Party> findAllByRequestDto(Pageable pageable, PartyFindRequestFilterDto requestFilterDto) {
        return manyPartyRepository.findAllByRequestDto(pageable, requestFilterDto);
    }
}
