package nbbang.com.nbbang.domain.party.service;


import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.party.domain.Hashtag;
import nbbang.com.nbbang.domain.party.repository.HashtagRepository;
import nbbang.com.nbbang.domain.party.repository.PartyHashtagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Hashtag findOrCreateByContent(String content) {
        Hashtag hashtag = findByContent(content);
        if(hashtag==null){
            hashtag = createHashtag(content);
        }
        return hashtag;
    }
}
