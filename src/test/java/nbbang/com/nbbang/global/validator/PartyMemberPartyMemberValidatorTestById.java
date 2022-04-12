package nbbang.com.nbbang.global.validator;

import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.partymember.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.error.exception.NotOwnerException;
import nbbang.com.nbbang.global.error.exception.NotPartyMemberException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PartyMemberPartyMemberValidatorTestById {

    @Autowired MemberRepository memberRepository;
    @Autowired PartyService partyService;
    @Autowired PartyMemberService partyMemberService;
    @Autowired PartyMemberValidator partyMemberValidator;

    @Test
    void ownerValidatorTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        Member savedMemberA = memberRepository.save(memberA);
        Member memberB = Member.builder().nickname("memberB").build();
        Member savedMemberB = memberRepository.save(memberB);
        Member memberC = Member.builder().nickname("memberC").build();
        Member savedMemberC = memberRepository.save(memberC);

        // when
        Party partyA = Party.builder().goalNumber(2).build();
        Party createdParty = partyService.create(partyA, savedMemberA.getId(), null);
        partyMemberService.joinParty(createdParty, savedMemberB);

        // then
        assertDoesNotThrow(()->{partyMemberValidator.validateOwner(createdParty, savedMemberA);});
        assertThrows(NotOwnerException.class,()->{partyMemberValidator.validateOwner(createdParty, savedMemberB);});
        assertThrows(NotOwnerException.class,()->{partyMemberValidator.validateOwner(createdParty, savedMemberC);});
    }

    @Test
    void memberValidatorTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        Member savedMemberA = memberRepository.save(memberA);
        Member memberB = Member.builder().nickname("memberB").build();
        Member savedMemberB = memberRepository.save(memberB);
        Member memberC = Member.builder().nickname("memberC").build();
        Member savedMemberC = memberRepository.save(memberC);

        // when
        Party partyA = Party.builder().goalNumber(2).build();
        Party createdParty = partyService.create(partyA, savedMemberA.getId(), null);
        partyMemberService.joinParty(createdParty, savedMemberB);

        partyMemberValidator.validatePartyMember(createdParty, savedMemberA);

        // then
        assertDoesNotThrow(()->{partyMemberValidator.validatePartyMember(createdParty, savedMemberA);});
        assertDoesNotThrow(()->{partyMemberValidator.validatePartyMember(createdParty, savedMemberB);});
        assertThrows(NotPartyMemberException.class,()->{partyMemberValidator.validatePartyMember(createdParty, savedMemberC);});

    }

}