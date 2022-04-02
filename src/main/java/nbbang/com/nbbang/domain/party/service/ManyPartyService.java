package nbbang.com.nbbang.domain.party.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.many.PartyListRequestFilterDto;
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

    public Page<Party> findAllParties(Pageable pageable, PartyListRequestFilterDto filter, Long cursorId, Long memberId) {
        return manyPartyRepository.findAllParties(pageable, filter, cursorId, memberId);
    }

}
