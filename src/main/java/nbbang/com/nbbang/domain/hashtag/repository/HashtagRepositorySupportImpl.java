package nbbang.com.nbbang.domain.hashtag.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.hashtag.domain.Hashtag;
import nbbang.com.nbbang.domain.hashtag.domain.PartyHashtag;
import nbbang.com.nbbang.domain.hashtag.domain.QHashtag;
import nbbang.com.nbbang.domain.hashtag.domain.QPartyHashtag;

import java.util.List;
import java.util.stream.Collectors;

import static nbbang.com.nbbang.domain.chat.domain.QMessage.message;
import static nbbang.com.nbbang.domain.partymember.domain.QPartyMember.partyMember;

@RequiredArgsConstructor
public class HashtagRepositorySupportImpl implements HashtagRepositorySupport{

    private final JPAQueryFactory query;

    public List<String> findAllByContents(List<String> hashtagContents) {
        QHashtag hashtag = QHashtag.hashtag;
        List<Hashtag> hashtags = query.selectFrom(hashtag)
                .where(hashtag.content.in(hashtagContents)).fetch();
        List<String> foundHashtagContents = hashtags.stream().map(h -> h.getContent()).collect(Collectors.toList());
        hashtagContents.removeAll(foundHashtagContents);
        return hashtagContents;
    }

    @Override
    public void deleteIfNotReferred(List<Hashtag> hashtags) {
        QHashtag hashtag = QHashtag.hashtag;
        QPartyHashtag partyHashtag = QPartyHashtag.partyHashtag;
        List<Hashtag> existingHashtags = query.select(partyHashtag.hashtag)
                .distinct()
                .from(partyHashtag)
                .where(partyHashtag.hashtag.in(hashtags)).fetch();
        
        existingHashtags.stream().forEach(h-> System.out.println("existingHashtag.getContent() = " + h.getContent()));

        hashtags.stream().forEach(h-> System.out.println("hashtag.getContent() = " + h.getContent()));
        
        query.delete(hashtag)
                .where(hashtag.in(hashtags))
                .where(hashtag.notIn(existingHashtags)).execute();

    }
}
