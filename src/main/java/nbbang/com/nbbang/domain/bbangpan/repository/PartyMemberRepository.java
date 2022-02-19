package nbbang.com.nbbang.domain.bbangpan.repository;

import nbbang.com.nbbang.domain.bbangpan.domain.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {

    PartyMember findByMemberIdAndPartyId(Long memberId, Long partyId);
}