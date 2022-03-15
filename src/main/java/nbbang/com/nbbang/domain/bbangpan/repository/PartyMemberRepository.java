package nbbang.com.nbbang.domain.bbangpan.repository;

import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface PartyMemberRepository extends JpaRepository<PartyMember, Long>, PartyMemberRepositorySupport {

    PartyMember findByMemberIdAndPartyId(Long memberId, Long partyId);
}
