package nbbang.com.nbbang.domain.hashtag.repository;

import nbbang.com.nbbang.domain.hashtag.domain.Hashtag;

import java.util.List;

public interface HashtagRepositorySupport {
    void deleteIfNotReferred(List<Hashtag> hashtags);
}
