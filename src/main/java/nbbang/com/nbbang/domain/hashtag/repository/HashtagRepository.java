package nbbang.com.nbbang.domain.hashtag.repository;

import nbbang.com.nbbang.domain.hashtag.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long>, HashtagRepositorySupport {
    List<Hashtag> findAllByContentIn(Iterable<String> contents);

}
