package nbbang.com.nbbang.domain.chat.service;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.controller.ChatRoomController;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class ChatReadTest {

    @Autowired ChatService chatService;
    @Autowired PartyService partyService;
    @Autowired MemberService memberService;
    @Autowired PartyMemberService partyMemberService;
    @Autowired MessageService messageService;
    @Autowired ChatRoomController chatRoomController;

    @Test
    void readMessageTest() {
        Long partyId = 1L;
        join(partyId, 4L); // 파티 1번에는 1~4까지 4명이 존재함
        partyService.updateActiveNumber(partyId, 1); // 1번 파티 입장
        chatRoomController.readMessage(partyId, 1L); // 1번 파티 입장
        Long messageId1 = messageService.send(partyId, 1L, "hello");
        Assertions.assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(1);

        partyService.updateActiveNumber(partyId, 1); // 2번 파티 입장
        chatService.readMessage(partyId, 2L); // 2번 파티 입장
        Assertions.assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(2);

        Long messageId2 = messageService.send(partyId, 2L, "hello here 2");
        Assertions.assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(2);
        Assertions.assertThat(messageService.findById(messageId2).getReadNumber()).isEqualTo(2);

        partyService.updateActiveNumber(partyId, -1); // 1번 파티 나감
        chatService.exitChat(partyId, 1L); // 1번 파티 나감
        Assertions.assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(2);

        partyService.updateActiveNumber(partyId, 1); // 3번 파티 입장
        chatService.readMessage(partyId, 3L); // 3번 파티 입장
        Assertions.assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(3);
        Assertions.assertThat(messageService.findById(messageId2).getReadNumber()).isEqualTo(3);
        Long messageId3 = messageService.send(partyId, 3L, "hello here 3");
        Assertions.assertThat(messageService.findById(messageId3).getReadNumber()).isEqualTo(2);

        partyService.updateActiveNumber(partyId, 1); // 1번 파티 재입장
        chatService.readMessage(partyId, 1L); // 1번 파티 재입장
        Assertions.assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(3);
        Assertions.assertThat(messageService.findById(messageId2).getReadNumber()).isEqualTo(3);
        Assertions.assertThat(messageService.findById(messageId3).getReadNumber()).isEqualTo(3);

    }

    void join(Long partyId, Long memberId) {
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);
        partyMemberService.joinParty(party, member);
    }
}