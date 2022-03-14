package nbbang.com.nbbang.domain.member.controller;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.service.MessageService;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.my.MyPartyListResponseDto;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.socket.ChatRoomService;
import nbbang.com.nbbang.global.socket.StompChannelInterceptor;
import nbbang.com.nbbang.global.support.controller.ControllerTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static nbbang.com.nbbang.domain.member.dto.Place.SINCHON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@Import({ControllerTestUtil.class})
@Slf4j
@Transactional
@ActiveProfiles("test")
class MyPartiesIntegrationTest {

    @Autowired ControllerTestUtil controllerTestUtil;
    @Autowired PartyService partyService;
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired PartyMemberService partyMemberService;
    @Autowired MessageService messageService;
    @Autowired StompChannelInterceptor stompChannelInterceptor;
    @Autowired
    ChatRoomService chatRoomService;

    @Test
    void partiesOn() throws Exception {


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
        messageService.send(partyId, saveMember1.getId(), "hello");

        chatRoomService.exit(member1Attributes, partyId); // 1번 파티 나감

        chatRoomService.enter(member2Attributes, partyId); // 2번 파티 입장

        messageService.send(partyId, saveMember2.getId(), "hello here 2");

        chatRoomService.enter(member3Attributes, partyId); // 3번 파티 입장

        messageService.send(partyId, saveMember3.getId(), "hello here 3");


        DefaultResponse res = controllerTestUtil.expectDefaultResponseObject(get("/members/parties/on"));
        MyPartyListResponseDto result = controllerTestUtil.convert(res.getData(), MyPartyListResponseDto.class);
        // assertThat(result.getParties().get(0).getNotReadNumber()).isEqualTo(2);

        chatRoomService.enter(member1Attributes, partyId); // 1번 파티 입장

        DefaultResponse res2 = controllerTestUtil.expectDefaultResponseObject(get("/members/parties/on"));
        MyPartyListResponseDto result2 = controllerTestUtil.convert(res2.getData(), MyPartyListResponseDto.class);
        // assertThat(result2.getParties().get(0).getNotReadNumber()).isEqualTo(0);

        messageService.send(partyId, saveMember3.getId(), "hoho 3");


        DefaultResponse res3 = controllerTestUtil.expectDefaultResponseObject(get("/members/parties/on"));
        MyPartyListResponseDto result3 = controllerTestUtil.convert(res3.getData(), MyPartyListResponseDto.class);
        // assertThat(result3.getParties().get(0).getNotReadNumber()).isEqualTo(0);

        chatRoomService.exit(member1Attributes, partyId); // 1번 파티 나감

    }

    void join(Long partyId, Long memberId) {
        Party party = partyService.findById(partyId);
        Member member = memberService.findById(memberId);
        partyMemberService.joinParty(party, member);
    }
}