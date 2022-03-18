package nbbang.com.nbbang.domain.chat.service;

import nbbang.com.nbbang.domain.chat.controller.ChatRoomController;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ChatServiceTest {
    @Autowired MessageRepository messageRepository;
    @Autowired ChatService chatService;
    @Autowired PartyRepository partyRepository;
    @Autowired MemberRepository memberRepository;


    @Test
    public void sendAndFindMessageTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party partyA = Party.builder().owner(memberA).build();
        partyRepository.save(partyA);
        String content = "hello 뿌링클";
        // when
        Long savedMessageId = chatService.sendMessage(memberA.getId(), partyA.getId(), content, LocalDateTime.now());
        // then
        Message findMessage = chatService.findById(savedMessageId);
        assertThat(findMessage.getContent().equals(content));
        assertThat(findMessage.getSender().equals(memberA));
        assertThat(findMessage.getParty().equals(partyA));
    }

    @Test
    public void sendAndFindManyMessageTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party partyA = Party.builder().owner(memberA).build();
        partyRepository.save(partyA);
        Party partyB = Party.builder().owner(memberA).build();
        partyRepository.save(partyB);
        String content = "뿌링클";
        // when
        for (int i = 0; i < 20; i++) {
            Long savedMessageId = chatService.sendMessage(memberA.getId(), partyA.getId(), content, LocalDateTime.now());
        }
        // then
        Page<Message> findMessages = chatService.findMessages(partyA, memberA.getId(), PageRequest.of(0, 10));
        assertThat(findMessages.getContent().size() == 10);

        findMessages = chatService.findMessages(partyA,  memberA.getId(),PageRequest.of(1, 10));
        assertThat(findMessages.getContent().size() == 10);

        findMessages = chatService.findMessages(partyA,  memberA.getId(),PageRequest.of(0, 20));
        assertThat(findMessages.getContent().size() == 20);

        findMessages = chatService.findMessages(partyB,  memberA.getId(),PageRequest.of(0, 10));
        assertThat(findMessages.getContent().size() == 0);
    }

    @Test
    public void findLastMessageIdTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party partyA = Party.builder().owner(memberA).build();
        partyRepository.save(partyA);
        Party partyB = Party.builder().owner(memberA).build();
        partyRepository.save(partyB);
        String content = "뿌링클";
        // when
        for (int i = 0; i < 20; i++) {
            chatService.sendMessage(memberA.getId(), partyA.getId(), content, LocalDateTime.now());
        }
        Long saveLastMessageId = chatService.sendMessage(memberA.getId(), partyA.getId(), content, LocalDateTime.now());
        // then
        Long findLastMessageId = chatService.findLastMessage(partyA).getId();
        assertThat(findLastMessageId.equals(saveLastMessageId));
        System.out.println(saveLastMessageId);
    }

    @Test
    public void findMessagesByCursorTest1() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party partyA = Party.builder().owner(memberA).build();
        partyRepository.save(partyA);
        String content = "처음";
        chatService.sendMessage(memberA.getId(), partyA.getId(), content, LocalDateTime.now());
        content = "뿌링클";
        for (int i = 0; i < 20; i++) {
            chatService.sendMessage(memberA.getId(), partyA.getId(), content, LocalDateTime.now());
        }
        String targ1 = "target1";
        chatService.sendMessage(memberA.getId(), partyA.getId(), targ1, LocalDateTime.now());
        content = "뿌링클";
        for (int i = 0; i < 8; i++) {
            chatService.sendMessage(memberA.getId(), partyA.getId(), content, LocalDateTime.now());
        }
        String targ2 = "target2";
        Long idx = chatService.sendMessage(memberA.getId(), partyA.getId(), targ2, LocalDateTime.now());
        content = "햄버거";
        for (int i = 0; i < 20; i++) {
            chatService.sendMessage(memberA.getId(), partyA.getId(), content, LocalDateTime.now());
        }
        // when
        Page<Message> findMessages = chatService.findMessagesByCursorId(partyA, memberA.getId(), PageRequest.of(0, 10), idx);
        // then
        findMessages.getContent().stream().forEach(m -> System.out.println(m.getContent()));
        assertThat(findMessages.getContent().get(0).getContent().equals(targ1));
        assertThat(findMessages.getContent().get(9).getContent().equals(targ2));
    }
}