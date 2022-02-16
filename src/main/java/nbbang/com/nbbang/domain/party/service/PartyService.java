package nbbang.com.nbbang.domain.party.service;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepository partyRepository;
    private final HashtagService hashtagService;

    public Page<Party> findAll(Pageable pageable) {
        return partyRepository.findAll(pageable);
    }

    public Long createParty(Party party, List<String> hashtags) {
        Party savedParty = partyRepository.save(party);
        Long partyId = savedParty.getId();
        for (String content: hashtags){
            hashtagService.createHashtag(partyId, content);
        }
        return savedParty.getId();
    }

    public void deleteParty(Long partyId) {
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NotFoundException("There is no party"));
        partyRepository.delete(party);
    }
}
