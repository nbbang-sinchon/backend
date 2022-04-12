package nbbang.com.nbbang.domain.party.service;

import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.domain.party.repository.PartyWishlistRepository;
import nbbang.com.nbbang.global.error.exception.UserException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PartyWishlistServiceTest {
    @Autowired PartyWishlistService partyWishlistService;
    @Autowired MemberRepository memberRepository;
    @Autowired PartyRepository partyRepository;
    @Autowired PartyWishlistRepository partyWishlistRepository;

    @Test
    public void addWishlistRepositoryTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party partyA = Party.builder().owner(memberA).goalNumber(10).status(PartyStatus.OPEN).build();
        partyRepository.save(partyA);
        // when
        partyWishlistService.addWishlistIfNotDuplicate(partyA.getId(), memberA.getId());
        // then
        assertThat(partyWishlistRepository.findByPartyIdAndMemberId(partyA.getId(), memberA.getId()).isPresent());
    }

    @Test
    public void addDuplicateWishlistTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party partyA = Party.builder().owner(memberA).goalNumber(10).status(PartyStatus.OPEN).build();
        partyRepository.save(partyA);
        // when
        partyWishlistService.addWishlistIfNotDuplicate(partyA.getId(), memberA.getId());
        // then
        assertThrows(UserException.class, () ->
            partyWishlistService.addWishlistIfNotDuplicate(partyA.getId(), memberA.getId()));
    }

    @Test
    public void deleteWishlistTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party partyA = Party.builder().owner(memberA).goalNumber(10).status(PartyStatus.OPEN).build();
        partyRepository.save(partyA);
        // when
        partyWishlistService.addWishlistIfNotDuplicate(partyA.getId(), memberA.getId());
        // then
        partyWishlistService.deleteWishlist(partyA.getId(), memberA.getId());
    }

    @Test
    public void deleteNotExistingWishlistTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party partyA = Party.builder().owner(memberA).goalNumber(10).status(PartyStatus.OPEN).build();
        partyRepository.save(partyA);
        // when

        // then
        assertThrows(NotFoundException.class, () ->
                partyWishlistService.deleteWishlist(partyA.getId(), memberA.getId()));
    }

}