package nbbang.com.nbbang.domain.party.service;


import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Hashtag;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyHashtag;
import nbbang.com.nbbang.domain.party.repository.HashtagRepository;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class HashtagService {
    private final PartyRepository partyRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional
    public Long createHashtag(Long partyId, String content){
        Party party = partyRepository.findById(partyId).get();
        PartyHashtag partyHashtag = PartyHashtag.createPartyHashtag(party);
        Hashtag hashtag = Hashtag.createHashtag(content, partyHashtag);
        hashtagRepository.save(hashtag);
        return hashtag.getId();
    }


}
