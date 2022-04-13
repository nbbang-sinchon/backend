package nbbang.com.nbbang.domain.hashtag.repository;

import nbbang.com.nbbang.domain.hashtag.domain.PartyHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartyHashtagRepository extends JpaRepository<PartyHashtag, Long> {
    List<PartyHashtag> findByHashtagId(Long hashtagId);
}
