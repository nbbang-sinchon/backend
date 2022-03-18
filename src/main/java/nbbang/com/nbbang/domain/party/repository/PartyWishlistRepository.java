package nbbang.com.nbbang.domain.party.repository;


import nbbang.com.nbbang.domain.party.domain.PartyWishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyWishlistRepository extends JpaRepository<PartyWishlist, Long> {
    Optional<PartyWishlist> findByMemberIdAndPartyId(Long memberId, Long partyId);
}
