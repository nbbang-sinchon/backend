package nbbang.com.nbbang.domain.chat.service;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.controller.ChatRoomController;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static nbbang.com.nbbang.domain.member.dto.Place.*;

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

    @Test
    void readMessageTest() {
        Member member = Member.builder().nickname("test member").build();
        Member saveMember1 = memberRepository.save(member);
        Party party = Party.builder().title("tempParty").place(SINCHON).goalNumber(4).build();
        Long partyId = partyService.create(party,saveMember1.getId(), null);
        Member member2 = Member.builder().nickname("test member").build();
        Member saveMember2 = memberRepository.save(member2);
        Member member3 = Member.builder().nickname("test member").build();
        Member saveMember3 = memberRepository.save(member3);


        join(partyId, saveMember2.getId());
        join(partyId, saveMember3.getId());
        // 파티에는 1~3까지 3명이 존재함

        partyService.updateActiveNumber(partyId, 1); // 1번 파티 입장
        chatService.readMessage(partyId, saveMember1.getId()); // 1번 파티 입장
        Long messageId1 = messageService.send(partyId, saveMember1.getId(), "hello");
        Assertions.assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(1);

        partyService.updateActiveNumber(partyId, 1); // 2번 파티 입장
        chatService.readMessage(partyId, saveMember2.getId()); // 2번 파티 입장
        Assertions.assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(2);

        Long messageId2 = messageService.send(partyId, saveMember2.getId(), "hello here 2");
        Assertions.assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(2);
        Assertions.assertThat(messageService.findById(messageId2).getReadNumber()).isEqualTo(2);

        partyService.updateActiveNumber(partyId, -1); // 1번 파티 나감
        chatService.exitChatRoom(partyId, saveMember1.getId()); // 1번 파티 나감
        Assertions.assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(2);

        partyService.updateActiveNumber(partyId, 1); // 3번 파티 입장
        chatService.readMessage(partyId, saveMember3.getId()); // 3번 파티 입장
        Assertions.assertThat(messageService.findById(messageId1).getReadNumber()).isEqualTo(3);
        Assertions.assertThat(messageService.findById(messageId2).getReadNumber()).isEqualTo(3);

        Long messageId3 = messageService.send(partyId, saveMember3.getId(), "hello here 3");
        Assertions.assertThat(messageService.findById(messageId3).getReadNumber()).isEqualTo(2);

        partyService.updateActiveNumber(partyId, 1); // 1번 파티 재입장
        chatService.readMessage(partyId, saveMember1.getId()); // 1번 파티 재입장
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