package nbbang.com.nbbang.domain.hashtag.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.domain.hashtag.domain.Hashtag;
import nbbang.com.nbbang.domain.hashtag.domain.QHashtag;

import java.util.List;
import java.util.stream.Collectors;

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
}
