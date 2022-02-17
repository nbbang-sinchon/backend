package nbbang.com.nbbang.domain.party.service;


import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Hashtag;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyHashtag;
import nbbang.com.nbbang.domain.party.repository.HashtagRepository;
import nbbang.com.nbbang.domain.party.repository.PartyHashtagRepository;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class HashtagService {
    private final PartyRepository partyRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional
    public void createHashtag(Long partyId, String content){
        Party party = partyRepository.findById(partyId).orElseThrow(() -> new NotFoundException("There is no party"));
        PartyHashtag partyHashtag = PartyHashtag.createPartyHashtag(party);
        List<Hashtag> hashtags = hashtagRepository.findByContent(content);
        if(hashtags.isEmpty()){
            Hashtag hashtag = Hashtag.createHashtag(content, partyHashtag);
            hashtagRepository.save(hashtag);
        }
        else{
            Hashtag hashtag = hashtags.get(0);
            partyHashtag.mapHashtag(hashtag);
        }
    }
}
