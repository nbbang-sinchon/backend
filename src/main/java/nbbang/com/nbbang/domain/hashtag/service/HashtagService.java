package nbbang.com.nbbang.domain.hashtag.service;


import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.hashtag.domain.Hashtag;
import nbbang.com.nbbang.domain.hashtag.repository.HashtagRepository;
import nbbang.com.nbbang.domain.hashtag.repository.PartyHashtagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Hashtag> findByContents(List<String> contents) {
        System.out.println("HashtagService.findByContents");
        List<Hashtag> hashtags = hashtagRepository.findAllByContentIn(contents);
        System.out.println("contents = " + contents);
        for (Hashtag hashtag : hashtags) {
            System.out.println("hashtag.getContent() = " + hashtag.getContent());
        }

        return Optional.ofNullable(hashtags).orElseGet(Collections::emptyList)
                .stream().filter(h->contents.contains(h.getContent())).collect(Collectors.toList());
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
