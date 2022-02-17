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

import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.PARTY_NOT_FOUND;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;
    private final PartyHashtagRepository partyHashtagRepository;

    public Hashtag createHashtag(String content) {
        Hashtag hashtag = Hashtag.createHashtag(content);
        hashtagRepository.save(hashtag);
        return hashtag;
    }

    public Hashtag findByContent(String content) {
        return hashtagRepository.findByContent(content);
    }

    public void deleteIfNotReferred(Hashtag hashtag) {
        if (partyHashtagRepository.findByHashtagId(hashtag.getId()).size()==0){
            hashtagRepository.delete(hashtag);
        }
    }
}
