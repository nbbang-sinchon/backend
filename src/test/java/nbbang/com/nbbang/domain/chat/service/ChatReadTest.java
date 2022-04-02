package nbbang.com.nbbang.domain.chat.service;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.socket.ChatRoomService;
import nbbang.com.nbbang.global.socket.StompChannelInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static nbbang.com.nbbang.domain.member.dto.Place.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Slf4j
class ChatReadTest {
    @Autowired PartyService partyService;
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired PartyMemberService partyMemberService;
    @Autowired
    MessageService messageService;
    @Autowired StompChannelInterceptor stompChannelInterceptor;
    @Autowired ChatRoomService chatRoomService;

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


        //when
        Map<String, Object> member1Attributes = new HashMap<>();
        member1Attributes.put("memberId", saveMember1.getId());
        Map<String, Object> member2Attributes = new HashMap<>();
        member2Attributes.put("memberId", saveMember2.getId());
        Map<String, Object> member3Attributes = new HashMap<>();
        member3Attributes.put("memberId", saveMember3.getId());

        //
        stompChannelInterceptor.connect(member1Attributes);
        stompChannelInterceptor.connect(member2Attributes);
        stompChannelInterceptor.connect(member3Attributes);

        chatRoomService.enter(member1Attributes, partyId); // 1번 파티 입장
        chatRoomService.readMessage(partyId, saveMember1.getId(), false);
        Long messageId1 = messageService.send(partyId, saveMember1.getId(), "hello").getId();
        assertThat(messageService.findById(messageId1).getNotReadNumber()).isEqualTo(2);

        chatRoomService.enter(member2Attributes, partyId); // 2번 파티 입장
        chatRoomService.readMessage(partyId, saveMember2.getId(), false);
        assertThat(messageService.findById(messageId1).getNotReadNumber()).isEqualTo(1);

        Long messageId2 = messageService.send(partyId, saveMember2.getId(), "hello here 2").getId();
        assertThat(messageService.findById(messageId1).getNotReadNumber()).isEqualTo(1);
        assertThat(messageService.findById(messageId2).getNotReadNumber()).isEqualTo(1);

        chatRoomService.exit(member1Attributes, partyId); // 1번 파티 나감
        assertThat(messageService.findById(messageId1).getNotReadNumber()).isEqualTo(1);

        chatRoomService.enter(member3Attributes, partyId); // 3번 파티 입장
        chatRoomService.readMessage(partyId, saveMember3.getId(), false);


        assertThat(messageService.findById(messageId1).getNotReadNumber()).isEqualTo(0);
        assertThat(messageService.findById(messageId2).getNotReadNumber()).isEqualTo(0);

        Long messageId3 = messageService.send(partyId, saveMember3.getId(), "hello here 3").getId();
        assertThat(messageService.findById(messageId3).getNotReadNumber()).isEqualTo(1);

        chatRoomService.enter(member1Attributes, partyId); // 1번 파티 입장
        chatRoomService.readMessage(partyId, saveMember1.getId(), false);

        assertThat(messageService.findById(messageId1).getNotReadNumber()).isEqualTo(0);
        assertThat(messageService.findById(messageId2).getNotReadNumber()).isEqualTo(0);
        assertThat(messageService.findById(messageId3).getNotReadNumber()).isEqualTo(0);

    }

    void join(Long partyId, Long memberId) {
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);
        partyMemberService.joinParty(party, member);
    }

}