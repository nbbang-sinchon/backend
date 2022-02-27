package nbbang.com.nbbang.domain.party.repository;

import nbbang.com.nbbang.domain.party.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Hashtag findByContent(String content);

}
