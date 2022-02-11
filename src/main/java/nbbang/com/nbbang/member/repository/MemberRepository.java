package nbbang.com.nbbang.member.repository;

import nbbang.com.nbbang.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
