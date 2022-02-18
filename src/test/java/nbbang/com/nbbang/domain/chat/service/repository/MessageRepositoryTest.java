package nbbang.com.nbbang.domain.chat.service.repository;

import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MessageRepositoryTest {
    @Autowired
    MessageRepository messageRepository;
    @Autowired ChatService chatService;
    @Autowired PartyRepository partyRepository;
    @Autowired MemberRepository memberRepository;

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
}