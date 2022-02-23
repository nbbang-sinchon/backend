package nbbang.com.nbbang.domain.member.repository;

import nbbang.com.nbbang.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);
}
