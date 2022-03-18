package nbbang.com.nbbang.domain.party.service;


import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Hashtag;
import nbbang.com.nbbang.domain.party.repository.HashtagRepository;
import nbbang.com.nbbang.domain.party.repository.PartyHashtagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;
    private final PartyHashtagRepository partyHashtagRepository;

    @Transactional
    public Hashtag createHashtag(String content) {
        Hashtag hashtag = Hashtag.createHashtag(content);
        hashtagRepository.save(hashtag);
        return hashtag;
    }

    public Hashtag findByContent(String content) {
        List<Hashtag> hashtags = hashtagRepository.findByContent(content);
        return Optional.ofNullable(hashtags).orElseGet(Collections::emptyList)
                .stream().filter(h->h.getContent().equals(content)).findAny().orElse(null);
    }

    @Transactional
    public void deleteIfNotReferred(Hashtag hashtag) {
        if (partyHashtagRepository.findByHashtagId(hashtag.getId()).size()==0){
            hashtagRepository.delete(hashtag);
        }
    }
    @Transactional
    public Hashtag findOrCreateByContent(String content) {
        Hashtag hashtag = findByContent(content);
        if(hashtag==null){
            hashtag = createHashtag(content);
        }
        return hashtag;
    }

    // ************** 구현 필요(쿼리 최적화) ************** /
    public List<Hashtag> findOrCreateByContent(List<String> hashtagContents) {
        return null;
    }
}
