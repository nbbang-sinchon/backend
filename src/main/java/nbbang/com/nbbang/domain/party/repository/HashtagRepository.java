package nbbang.com.nbbang.domain.party.repository;

import nbbang.com.nbbang.domain.party.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
