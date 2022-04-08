package nbbang.com.nbbang.domain.party.service;

import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.dto.single.request.PartyRequestDto;
import nbbang.com.nbbang.domain.party.exception.PartyExitForbiddenException;
import nbbang.com.nbbang.domain.party.exception.PartyJoinException;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.domain.partymember.service.PartyMemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PartyServiceTest {
    @Autowired PartyService partyService;
    @Autowired MemberRepository memberRepository;
    @Autowired PartyRepository partyRepository;
    @Autowired
    PartyMemberService partyMemberService;


    @Test
    public void partyJoin_BasicTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Member memberB = Member.builder().nickname("memberB").build();
        memberRepository.save(memberB);
        Member memberC = Member.builder().nickname("memberC").build();
        memberRepository.save(memberC);
        Party partyA = Party.builder().owner(memberA).goalNumber(10).status(PartyStatus.OPEN).build();
        partyRepository.save(partyA);
        // when
        partyMemberService.joinParty(partyA, memberB);
        partyMemberService.joinParty(partyA, memberC);
        // then
        assertThat(partyA.getPartyMembers().size()).isEqualTo(2);
        assertThat(partyA.getPartyMembers().get(0).getMember().equals(memberB));
    }

    @Test
    public void partyJoin_DuplicateJoinTest1() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        Member saveMember = memberRepository.save(memberA);
        Party partyA = Party.builder().owner(memberA).goalNumber(10).status(PartyStatus.OPEN).build();
        partyService.create(partyA, saveMember.getId(), null);
        // when // then
        assertThrows(PartyJoinException.class, () -> {
            partyMemberService.joinParty(partyA, memberA);
        });
    }

    @Test
    public void partyJoin_DuplicateJoinTest2() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Member memberB = Member.builder().nickname("memberB").build();
        memberRepository.save(memberB);
        Party partyA = Party.builder().owner(memberA).status(PartyStatus.OPEN).goalNumber(2).build();
        partyRepository.save(partyA);
        // when
        partyMemberService.joinParty(partyA, memberB);
        // then
        assertThrows(PartyJoinException.class, () -> {
            partyMemberService.joinParty(partyA, memberB);});
    }

    @Test
    public void partyJoin_AlreadyFullTest1() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Member memberB = Member.builder().nickname("memberB").build();
        memberRepository.save(memberB);
        Party partyA = Party.builder().owner(memberA).goalNumber(1).status(PartyStatus.OPEN).build();
        partyRepository.save(partyA);
        // when
        partyMemberService.joinParty(partyA, memberB);
        // then
        assertThrows(PartyJoinException.class, () -> {
            partyMemberService.joinParty(partyA, memberB);});
    }


    @Test
    public void partyExit_BasicTest1() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Member memberB = Member.builder().nickname("memberB").build();
        memberRepository.save(memberB);
        Party partyA = Party.builder().owner(memberA).goalNumber(3).status(PartyStatus.OPEN).build();
        partyService.create(partyA, memberA.getId(), null);
        // partyRepository.save(partyA);
        // when
        partyMemberService.joinParty(partyA, memberB);
        // then
        //partyService.exitParty(partyA, memberA); // 방장이 나가는 경우
        //assertThat(partyA.getIsBlocked()).isEqualTo(true);
        assertThrows(PartyExitForbiddenException.class, () -> partyMemberService.exitParty(partyA, memberA)); // 로직 변경; 방장은 나갈 수 없습니다.
    }


    @Test
    public void partyExit_BasicTest2() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Member memberB = Member.builder().nickname("memberB").build();
        memberRepository.save(memberB);
        Party partyA = Party.builder().owner(memberA).goalNumber(3).status(PartyStatus.OPEN).build();
        partyRepository.save(partyA);
        // when
        partyMemberService.joinParty(partyA, memberB);
        // then
        partyMemberService.exitParty(partyA, memberB);
        assertThat(partyA.getPartyMembers().size()).isEqualTo(0);
    }

    @Test
    public void partyFindTest() {
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party partyA = Party.builder().title("partyA").owner(memberA).goalNumber(3).status(PartyStatus.OPEN).build();
        partyRepository.save(partyA);

        Party find = partyRepository.findById(partyA.getId()).get();
        assertThat(find).isEqualTo(partyA);
        assertThat(find.getTitle()).isEqualTo("partyA");
    }

    @Test
    public void partySaveAndFindTest() {
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        PartyRequestDto dto = PartyRequestDto.builder().title("party").goalNumber(4).content("hello").hashtags(Arrays.asList("치킨")).place("sinchon").build();
        Party party = dto.createEntityByDto();
        partyService.create(party, memberA.getId(), dto.getHashtags());
        Party findParty = partyService.findById(party.getId());
        assertThat(findParty.getPartyHashtags().size()).isEqualTo(1);
    }

}