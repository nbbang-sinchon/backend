package nbbang.com.nbbang.domain.partymember.repository;

import nbbang.com.nbbang.domain.partymember.domain.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PartyMemberRepository extends JpaRepository<PartyMember, Long>, PartyMemberRepositorySupport {
    @Query("select pm from PartyMember pm where pm.member.id = ?1 and pm.party.id = ?2")
    PartyMember findByMemberIdAndPartyId(Long memberId, Long partyId);
}
