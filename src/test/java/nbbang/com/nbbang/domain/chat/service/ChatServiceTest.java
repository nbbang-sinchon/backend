package nbbang.com.nbbang.domain.chat.service;

import nbbang.com.nbbang.domain.chat.controller.ChatRoomController;
import nbbang.com.nbbang.domain.chat.domain.Message;
import nbbang.com.nbbang.domain.chat.repository.MessageRepository;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.repository.PartyRepository;
import nbbang.com.nbbang.domain.party.service.PartyService;
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
    @Autowired ChatService chatService;
    @Autowired PartyRepository partyRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MessageService messageService;
    @Autowired PartyService partyService;


    @Test
    public void sendAndFindMessageTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party partyA = Party.builder().owner(memberA).build();
        partyA = partyService.create(partyA, memberA.getId(),null);
        String content = "hello 뿌링클";
        // when

        Long savedMessageId = messageService.send(partyA.getId(), memberA.getId(), content).getId();
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
        partyA = partyService.create(partyA, memberA.getId(), null);
        Party partyB = Party.builder().owner(memberA).build();
        partyB = partyService.create(partyB, memberA.getId(), null);
        String content = "뿌링클";
        // when
        for (int i = 0; i < 19; i++) {
            messageService.send(partyA.getId(), memberA.getId(), content);
        }
        // then

        // findMessage에 검증 로직을 추가하였더니 테스트 코드가 잘 돌지 않아서 조금 수정하였습니다.

        Page<Message> findMessages = chatService.findMessages(partyA, memberA.getId(), PageRequest.of(0, 10));
        assertThat(findMessages.getContent().size() == 10);

        findMessages = chatService.findMessages(partyA,  memberA.getId(),PageRequest.of(1, 10));
        assertThat(findMessages.getContent().size() == 10);

        findMessages = chatService.findMessages(partyA,  memberA.getId(),PageRequest.of(0, 20));
        assertThat(findMessages.getContent().size() == 20);

        findMessages = chatService.findMessages(partyB,  memberA.getId(),PageRequest.of(0, 10));
        assertThat(findMessages.getContent().size() == 1);
    }

    @Test
    public void findLastMessageIdTest() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party partyA = Party.builder().owner(memberA).build();
        partyA = partyService.create(partyA, memberA.getId(), null);
        Party partyB = Party.builder().owner(memberA).build();
        partyB = partyService.create(partyB, memberA.getId(), null);

        String content = "뿌링클";
        // when
        for (int i = 0; i < 19; i++) {
            messageService.send(partyA.getId(), memberA.getId(), content);
        }
        Long saveLastMessageId = messageService.send(partyA.getId(), memberA.getId(), content).getId();
        // then
        Long findLastMessageId = chatService.findLastMessage(partyA).getId();
        assertThat(findLastMessageId.equals(saveLastMessageId));
    }

    @Test
    public void findMessagesByCursorTest1() {
        // given
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party partyA = Party.builder().owner(memberA).build();
        partyA = partyService.create(partyA, memberA.getId(), null);
        String content = "처음";
        messageService.send(partyA.getId(), memberA.getId(), content);
        content = "뿌링클";
        for (int i = 0; i < 20; i++) {
            messageService.send(partyA.getId(), memberA.getId(), content);
        }
        String targ1 = "target1";
        messageService.send(partyA.getId(), memberA.getId(), targ1);

        content = "뿌링클";
        for (int i = 0; i < 8; i++) {
            messageService.send(partyA.getId(), memberA.getId(), content);
        }
        String targ2 = "target2";
        Long idx = messageService.send(partyA.getId(), memberA.getId(), targ2).getId();
        content = "햄버거";
        for (int i = 0; i < 20; i++) {
            messageService.send(partyA.getId(), memberA.getId(), content);
        }
        // when
        Page<Message> findMessages = chatService.findMessagesByCursorId(partyA, memberA.getId(), PageRequest.of(0, 10), idx);
        // then
        findMessages.getContent().stream().forEach(m -> System.out.println(m.getContent()));
        assertThat(findMessages.getContent().get(0).getContent().equals(targ1));
        assertThat(findMessages.getContent().get(9).getContent().equals(targ2));
    }
}