package nbbang.com.nbbang.domain.party.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;

    public Page<Party> findAll(Pageable pageable) {
        return partyRepository.findAll(pageable);
    }

    public Long createParty(Party party) {
        Party savedParty = partyRepository.save(party);
        return savedParty.getId();
    }

    public void deleteParty(Long partyId) {
        Optional<Party> party = partyRepository.findById(partyId);
        partyRepository.delete(party.get()); // get 말고 좋은 방법을 생각해보자.
    }
}
