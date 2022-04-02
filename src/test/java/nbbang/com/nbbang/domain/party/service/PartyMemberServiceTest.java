package nbbang.com.nbbang.domain.party.service;

import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.service.MessageService;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static nbbang.com.nbbang.domain.member.dto.Place.SINCHON;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PartyMemberServiceTest {

    @Autowired PartyMemberService partyMemberService;
    @Autowired
    MessageService messageService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired PartyService partyService;
    @Autowired
    MemberService memberService;

    @Test
    void getFirstMessageId() {
        Member member = Member.builder().nickname("test member").build();
        Member saveMember1 = memberRepository.save(member);
        Party party = Party.builder().title("tempParty").place(SINCHON).goalNumber(4).build();
        Party savedParty = partyService.create(party, saveMember1.getId(), null);
        Long partyId = savedParty.getId();
        Member member2 = Member.builder().nickname("test member").build();
        Member saveMember2 = memberRepository.save(member2);

        join(partyId, saveMember2.getId());
       Message message1 = partyMemberService.getEnterMessage(partyId, saveMember2.getId());

        exit(partyId, saveMember2.getId());
        join(partyId, saveMember2.getId());


        Message message2 = partyMemberService.getEnterMessage(partyId, saveMember2.getId());
        Assertions.assertThat(message1.getContent()).isEqualTo(message2.getContent());
        Assertions.assertThat(message2.getId()).isGreaterThan(message1.getId());
    }

    void join(Long partyId, Long memberId) {
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);
        partyMemberService.joinParty(party, member);
    }

    void exit(Long partyId, Long memberId) {
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);
        partyMemberService.exitParty(party, member);
    }
}