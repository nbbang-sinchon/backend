package nbbang.com.nbbang.party.repository;

import nbbang.com.nbbang.party.domain.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
