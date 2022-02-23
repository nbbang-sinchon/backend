package nbbang.com.nbbang.domain.party.repository;

import nbbang.com.nbbang.domain.party.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    @Query(value = "select * from Hashtag where binary content = ?1", nativeQuery = true)
    Hashtag findByContent(String content);
}
