package nbbang.com.nbbang.domain.party.repository;


import nbbang.com.nbbang.domain.party.domain.PartyWishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PartyWishlistRepository extends JpaRepository<PartyWishlist, Long> {
    @Query("select pw from PartyWishlist pw where pw.member.id = ?1 and pw.party.id = ?2")
    Optional<PartyWishlist> findByMemberIdAndPartyId(Long memberId, Long partyId);
}
