package nbbang.com.nbbang.domain.party.service;

import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.exception.PartyExitForbiddenException;
import nbbang.com.nbbang.domain.party.exception.PartyJoinException;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PartyServiceTest {
    @Autowired PartyService partyService;
    @Autowired MemberRepository memberRepository;
    @Autowired PartyRepository partyRepository;
    @Autowired MemberPartyService memberPartyService;


    @Test
    public void partyJoin_BasicTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Member memberB = Member.builder().nickname("memberB").build();
        memberRepository.save(memberB);
        Member memberC = Member.builder().nickname("memberC").build();
        memberRepository.save(memberC);
        Party partyA = Party.builder().owner(memberA).goalNumber(10).isBlocked(false).status(PartyStatus.OPEN).build();
        partyRepository.save(partyA);
        // when
        memberPartyService.joinParty(partyA, memberB);
        memberPartyService.joinParty(partyA, memberC);
        // then
        assertThat(partyA.getMemberParties().size()).isEqualTo(2);
        assertThat(partyA.getMemberParties().get(0).getMember().equals(memberB));
    }

    @Test
    public void partyJoin_DuplicateJoinTest1() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party partyA = Party.builder().owner(memberA).goalNumber(10).isBlocked(false).status(PartyStatus.OPEN).build();
        partyRepository.save(partyA);
        // when // then
        assertThrows(PartyJoinException.class, () -> {memberPartyService.joinParty(partyA, memberA);});
    }

    @Test
    public void partyJoin_DuplicateJoinTest2() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Member memberB = Member.builder().nickname("memberB").build();
        memberRepository.save(memberB);
        Party partyA = Party.builder().owner(memberA).isBlocked(false).status(PartyStatus.OPEN).goalNumber(2).build();
        partyRepository.save(partyA);
        // when
        memberPartyService.joinParty(partyA, memberB);
        // then
        assertThrows(PartyJoinException.class, () -> {memberPartyService.joinParty(partyA, memberB);});
    }

    @Test
    public void partyJoin_AlreadyFullTest1() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Member memberB = Member.builder().nickname("memberB").build();
        memberRepository.save(memberB);
        Party partyA = Party.builder().owner(memberA).goalNumber(1).isBlocked(false).status(PartyStatus.OPEN).build();
        partyRepository.save(partyA);
        // when
        memberPartyService.joinParty(partyA, memberB);
        // then
        assertThrows(PartyJoinException.class, () -> {memberPartyService.joinParty(partyA, memberB);});
    }


    @Test
    public void partyExit_BasicTest1() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Member memberB = Member.builder().nickname("memberB").build();
        memberRepository.save(memberB);
        Party partyA = Party.builder().owner(memberA).goalNumber(3).isBlocked(false).status(PartyStatus.OPEN).build();
        partyRepository.save(partyA);
        // when
        memberPartyService.joinParty(partyA, memberB);
        // then
        //partyService.exitParty(partyA, memberA); // 방장이 나가는 경우
        //assertThat(partyA.getIsBlocked()).isEqualTo(true);
        assertThrows(PartyExitForbiddenException.class, () ->memberPartyService.exitParty(partyA, memberA)); // 로직 변경; 방장은 나갈 수 없습니다.
    }


    @Test
    public void partyExit_BasicTest2() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Member memberB = Member.builder().nickname("memberB").build();
        memberRepository.save(memberB);
        Party partyA = Party.builder().owner(memberA).goalNumber(3).status(PartyStatus.OPEN).isBlocked(false).build();
        partyRepository.save(partyA);
        // when
        memberPartyService.joinParty(partyA, memberB);
        // then
        memberPartyService.exitParty(partyA, memberB);
        assertThat(partyA.getMemberParties().size()).isEqualTo(0);
        assertThat(partyA.getIsBlocked()).isEqualTo(false);
    }
}