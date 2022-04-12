package nbbang.com.nbbang.domain.partymember.repository;

import nbbang.com.nbbang.domain.partymember.domain.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PartyMemberRepository extends JpaRepository<PartyMember, Long>, PartyMemberRepositorySupport {
    @Query("select pm from PartyMember pm where pm.party.id = :partyId and pm.member.id = :memberId")
    PartyMember findByPartyIdAndMemberId(@Param("partyId") Long partyId, @Param("memberId") Long memberId);
}
