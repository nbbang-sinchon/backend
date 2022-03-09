package nbbang.com.nbbang.domain.party.repository;

import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface PartyRepository extends JpaRepository<Party, Long>, PartyRepositorySupport{
    Page<Party> findAll(Pageable pageable);
    Party findByTitle(String title);
}
