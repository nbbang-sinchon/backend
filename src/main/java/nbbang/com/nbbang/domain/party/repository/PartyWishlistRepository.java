package nbbang.com.nbbang.domain.party.repository;


import nbbang.com.nbbang.domain.party.domain.PartyWishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PartyWishlistRepository extends JpaRepository<PartyWishlist, Long> {
    @Query("select pw from PartyWishlist pw where pw.party.id = :partyId and pw.member.id = :memberId")
    Optional<PartyWishlist> findByPartyIdAndMemberId(@Param("partyId") Long partyId, @Param("memberId") Long memberId);
}
