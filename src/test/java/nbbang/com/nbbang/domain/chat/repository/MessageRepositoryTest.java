package nbbang.com.nbbang.domain.chat.repository;

import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.chat.service.MessageService;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.domain.party.service.PartyService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MessageRepositoryTest {
    @Autowired
    MessageRepository messageRepository;
    @Autowired ChatService chatService;
    @Autowired PartyRepository partyRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired
    PartyService partyService;
    @Autowired
    MessageService messageService;

    @Test
    public void findLastMessageIdTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        Member memberB = Member.builder().nickname("memberB").build();
        Party partyA = Party.builder().owner(memberA).build();
        Party partyB = Party.builder().owner(memberB).build();

        // when

        // then

    }

    @Test
    public void countByPartyIdAndIdGreaterThan(){
        Member memberA = Member.builder().nickname("memberA").build();
        Member savedMember = memberRepository.save(memberA);
        Party partyA = Party.builder().title("hello").goalNumber(3).build();
        Party party = partyService.create(partyA, savedMember.getId(), null);
        messageService.send(party.getId(), savedMember.getId(), "hello");
        messageService.send(party.getId(), savedMember.getId(), "hello2");

        Integer cnt = messageRepository.countByPartyIdAndIdGreaterThan(party.getId(), 0L);
        Assertions.assertThat(cnt).isEqualTo(3);
    }
}