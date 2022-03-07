package nbbang.com.nbbang.domain.chat.service;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.controller.ChatRoomController;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.socket.Session.SessionMemberService;
import nbbang.com.nbbang.global.socket.StompChannelInterceptor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static nbbang.com.nbbang.domain.member.dto.Place.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Slf4j
class ChatReadTest {

    @Autowired ChatService chatService;
    @Autowired PartyService partyService;
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired PartyMemberService partyMemberService;
    @Autowired MessageService messageService;
    @Autowired ChatRoomController chatRoomController;
    @Autowired StompChannelInterceptor stompChannelInterceptor;
    @Autowired SessionMemberService sessionMemberService;

    @Test
    void readMessageTest() {
        //given
        Member member = Member.builder().nickname("test member").build();
        Member saveMember1 = memberRepository.save(member);
        Party party = Party.builder().title("tempParty").place(SINCHON).goalNumber(4).build();
        Party savedParty = partyService.create(party, saveMember1.getId(), null);
        Long partyId = savedParty.getId();
        Member member2 = Member.builder().nickname("test member").build();
        Member saveMember2 = memberRepository.save(member2);
        Member member3 = Member.builder().nickname("test member").build();
        Member saveMember3 = memberRepository.save(member3);

        join(partyId, saveMember2.getId());
        join(partyId, saveMember3.getId());
        String session1 = "session1";
        String session2 = "session2";
        String session3 = "session3";

        //when
        sessionMemberService.addSession(session1, saveMember1.getId());
        sessionMemberService.addSession(session2, saveMember2.getId());
        sessionMemberService.addSession(session3, saveMember3.getId());

        //
        stompChannelInterceptor.enterChatRoom(session1, partyId); // 1번 파티 입장
        Long messageId1 = messageService.send(partyId, saveMember1.getId(), "hello");
        assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(1);

        stompChannelInterceptor.enterChatRoom(session2, partyId); // 2번 파티 입장
        assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(2);

        Long messageId2 = messageService.send(partyId, saveMember2.getId(), "hello here 2");
        assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(2);
        assertThat(messageService.findById(messageId2).getReadNumber()).isEqualTo(2);

        stompChannelInterceptor.exitChatRoom(session1, partyId); // 1번 파티 나감
        assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(2);

        stompChannelInterceptor.enterChatRoom(session3, partyId); // 3번 파티 입장

        assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(3);
        assertThat(messageService.findById(messageId2).getReadNumber()).isEqualTo(3);

        Long messageId3 = messageService.send(partyId, saveMember3.getId(), "hello here 3");
        assertThat(messageService.findById(messageId3).getReadNumber()).isEqualTo(2);

        stompChannelInterceptor.enterChatRoom(session1, partyId); // 1번 파티 입장
        assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(3);
        assertThat(messageService.findById(messageId2).getReadNumber()).isEqualTo(3);
        assertThat(messageService.findById(messageId3).getReadNumber()).isEqualTo(3);

    }

    void join(Long partyId, Long memberId) {
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);
        partyMemberService.joinParty(party, member);
    }

}