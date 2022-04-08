package nbbang.com.nbbang.domain.partymember.repository;

import nbbang.com.nbbang.domain.partymember.domain.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PartyMemberRepository extends JpaRepository<PartyMember, Long>, PartyMemberRepositorySupport {

    PartyMember findByMemberIdAndPartyId(Long memberId, Long partyId);
}
