package nbbang.com.nbbang.domain.member.service;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.service.MessageService;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.partymember.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.socket.service.ChatRoomService;
import nbbang.com.nbbang.global.socket.StompChannelInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static nbbang.com.nbbang.domain.member.dto.Place.SINCHON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Slf4j
class MemberServiceTest {
    @Autowired PartyService partyService;
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired PartyMemberService partyMemberService;
    @Autowired
    MessageService messageService;
    @Autowired StompChannelInterceptor stompChannelInterceptor;
    @Autowired ChatRoomService chatRoomService;

    @Test
    void isThereNotReadChat() {
        Member member = Member.builder().nickname("test member").build();
        Member saveMember1 = memberRepository.save(member);
        Party party = Party.builder().title("tempParty").place(SINCHON).goalNumber(4).build();
        Party savedParty = partyService.create(party, saveMember1.getId(), null);
        Long partyId = savedParty.getId();
        Member member2 = Member.builder().nickname("test member").build();
        Member saveMember2 = memberRepository.save(member2);

        join(partyId, saveMember2.getId());

        //when
        Map<String, Object> member1Attributes = new HashMap<>();
        member1Attributes.put("memberId", saveMember1.getId());
        Map<String, Object> member2Attributes = new HashMap<>();
        member2Attributes.put("memberId", saveMember2.getId());

        stompChannelInterceptor.connect(member1Attributes);
        stompChannelInterceptor.connect(member2Attributes);

        chatRoomService.enter(member1Attributes, partyId); // 1번 파티 입장
        chatRoomService.readMessage(partyId, member.getId(), false);
        Long messageId1 = messageService.send(partyId, saveMember1.getId(), "hello").getId();

        assertThat(memberService.isThereNotReadChat(saveMember2.getId())).isEqualTo(true);

        chatRoomService.enter(member2Attributes, partyId); // 2번 파티 입장. 채팅 읽음.
        chatRoomService.readMessage(partyId, member2.getId(), false);

        assertThat(memberService.isThereNotReadChat(saveMember2.getId())).isEqualTo(false);
    }


    void join(Long partyId, Long memberId) {
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);
        partyMemberService.joinParty(party, member);
    }
}