package nbbang.com.nbbang.domain.party.repository;

import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.PartyFindRequestDto;
import org.springframework.data.domain.Page;

public interface ManyPartyRepositorySupport {
    Page<Party> findAllByRequestDto(PartyFindRequestDto requestDto);
}
