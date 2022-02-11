package nbbang.com.nbbang.party.repository;

import nbbang.com.nbbang.party.domain.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
