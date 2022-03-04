package nbbang.com.nbbang.global.validator;

import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.error.exception.NotOwnerException;
import nbbang.com.nbbang.global.error.exception.NotPartyMemberException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PartyMemberValidatorTest {

    @Autowired MemberRepository memberRepository;
    @Autowired PartyService partyService;
    @Autowired PartyMemberService partyMemberService;
    @Autowired PartyMemberValidator partyMemberValidator;
    @Autowired EntityManager em;

    @Test
    void ownerValidatorTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Member memberB = Member.builder().nickname("memberB").build();
        memberRepository.save(memberA);
        Member memberC = Member.builder().nickname("memberC").build();
        memberRepository.save(memberA);

        // when
        Party partyA = Party.builder().goalNumber(2).build();
        partyService.create(partyA, memberA.getId(), null);
        partyMemberService.joinParty(partyA, memberB);
        em.flush();
        em.clear();

        // then

        assertThatThrownBy(()->{partyMemberValidator.validateOwner(partyA.getId(), memberA.getId());})
                .doesNotThrowAnyException();
        assertDoesNotThrow(()->{partyMemberValidator.validateOwner(partyA.getId(), memberA.getId());});
        assertThrows(NotOwnerException.class,()->{partyMemberValidator.validateOwner(partyA.getId(), memberB.getId());});
        assertThrows(NotOwnerException.class,()->{partyMemberValidator.validateOwner(partyA.getId(), memberC.getId());});
    }
    @Test
    void memberValidatorTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Member memberB = Member.builder().nickname("memberB").build();
        memberRepository.save(memberA);
        Member memberC = Member.builder().nickname("memberC").build();
        memberRepository.save(memberA);

        // when
        Party partyA = Party.builder().goalNumber(2).build();
        partyService.create(partyA, memberA.getId(), null);
        partyMemberService.joinParty(partyA, memberB);
        em.flush();
        em.clear();

        // then
        assertThatThrownBy(()->{partyMemberValidator.validatePartyMember(partyA.getId(), memberA.getId());})
                .doesNotThrowAnyException();
        assertThatThrownBy(()->{partyMemberValidator.validatePartyMember(partyA.getId(), memberB.getId());})
                .doesNotThrowAnyException();
        assertThatThrownBy(()->{partyMemberValidator.validatePartyMember(partyA.getId(), memberC.getId());})
                .isInstanceOf(NotPartyMemberException.class);
    }


}