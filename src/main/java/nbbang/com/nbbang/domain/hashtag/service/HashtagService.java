package nbbang.com.nbbang.domain.hashtag.service;


import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.hashtag.domain.Hashtag;
import nbbang.com.nbbang.domain.hashtag.domain.PartyHashtag;
import nbbang.com.nbbang.domain.hashtag.repository.HashtagRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    public void linkHashtagsToParty(Party party, List<String> hashtagContents){
        if(hashtagContents!=null) {
            List<Hashtag> hashtags = findOrCreateByContents(hashtagContents);
            List<PartyHashtag> partyHashtags = new ArrayList<>();
            Optional.ofNullable(hashtags).orElseGet(Collections::emptyList).stream().forEach(hashtag -> partyHashtags.add(PartyHashtag.createPartyHashtag(party, hashtag)));
        }
    }

    private List<Hashtag> findOrCreateByContents(List<String> hashtagContents){
        List<Hashtag> hashtags = findByContents(hashtagContents);
        List<String> storedHashtags = hashtags.stream().map(hashtag -> hashtag.getContent()).collect(Collectors.toList());
        hashtagContents.removeAll(storedHashtags);
        hashtagContents.stream().forEach(content->hashtags.add(Hashtag.createHashtag(content)));
        return hashtags;
    }

    private List<Hashtag> findByContents(List<String> contents) {
        List<Hashtag> hashtags = hashtagRepository.findAllByContentIn(contents);
        return Optional.ofNullable(hashtags).orElseGet(Collections::emptyList)
                .stream().filter(h->contents.contains(h.getContent())).collect(Collectors.toList());
    }

    @Transactional
    public void detachHashtagsFromParty(Party party, List<String> oldHashtagContents) {
        List<PartyHashtag> partyHashtags = party.deletePartyHashtags(oldHashtagContents);
        List<Hashtag> hashtags = partyHashtags.stream().map(ph -> ph.getHashtag()).collect(Collectors.toList());
        hashtagRepository.deleteIfNotReferred(hashtags);
    }
}
