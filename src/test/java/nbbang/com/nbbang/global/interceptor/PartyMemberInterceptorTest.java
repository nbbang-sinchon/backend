package nbbang.com.nbbang.global.interceptor;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.dto.message.ChatSendRequestDto;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.member.repository.MemberRepository;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.single.request.PartyRequestDto;
import nbbang.com.nbbang.domain.party.dto.single.response.PartyIdResponseDto;
import nbbang.com.nbbang.domain.party.service.PartyMemberService;
import nbbang.com.nbbang.global.error.ErrorResponse;
import nbbang.com.nbbang.global.error.GlobalErrorResponseMessage;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.support.controller.ControllerTestUtil;
import nbbang.com.nbbang.domain.party.service.PartyService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static nbbang.com.nbbang.global.error.GlobalErrorResponseMessage.NOT_OWNER_ERROR;
import static nbbang.com.nbbang.global.response.StatusCode.FORBIDDEN;
import static nbbang.com.nbbang.global.response.StatusCode.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import({ControllerTestUtil.class, PartyMemberInterceptorTestDto.class})
@Slf4j
@Transactional
@ActiveProfiles("test")
class PartyMemberInterceptorTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ControllerTestUtil controllerTestUtil;
    @Autowired private MemberRepository memberRepository;
    @Autowired private PartyService partyService;
    @Autowired private PartyMemberService partyMemberService;
    @Autowired private PartyMemberInterceptorTestDto testDto;
    @Autowired private CurrentMember currentMember;

    @BeforeEach
    void support(){
        Member memberA = Member.createMember("방장", Place.SINCHON);
        Member owner = memberRepository.save(memberA);
        Member memberB = Member.createMember("파티 멤버", Place.SINCHON);
        Member partyMember = memberRepository.save(memberB);
        Member memberC = Member.createMember("그냥 멤버", Place.SINCHON);
        Member justMember = memberRepository.save(memberC);
        Party party = Party.builder().title("testParty").goalNumber(3).build();
        Party createdParty = partyService.create(party, owner.getId(), null);
        partyMemberService.joinParty(createdParty, partyMember);
        testDto.change(owner.getId(), partyMember.getId(), justMember.getId(), createdParty.getId());
    }

    @Test
    void ownerSuccess() throws Exception {
        mockMvc.perform(get("/members/develop/{memberId}/login", testDto.getOwnerId()));
        DefaultResponse sendChatRes = controllerTestUtil.expectDefaultResponseObject(get("/chats/{partyId}", testDto.getPartyId()));
        Assertions.assertThat(sendChatRes.getStatusCode()).isEqualTo(OK);

        PartyRequestDto partyRequestDto = PartyRequestDto.builder().hashtags(Arrays.asList("hello world")).build();
        DefaultResponse updatePartyRes = controllerTestUtil
                .expectDefaultResponseObject(patch("/parties/{partyId}", testDto.getPartyId()), partyRequestDto);
        Assertions.assertThat(updatePartyRes.getStatusCode()).isEqualTo(OK);
    }


// ************로그인 완료 후 테스트 진행 ******************//
/*    @Test
    void partyMemberUpdatePartyFail() throws Exception {
       mockMvc.perform(get("/members/develop/{memberId}/login", testDto.getPartyMemberId()));
        PartyRequestDto partyRequestDto = PartyRequestDto.builder().hashtags(Arrays.asList("hash","tag")).build();
        ErrorResponse updatePartyRes = controllerTestUtil
                .expectErrorResponseObject(patch("/parties/{partyId}", testDto.getPartyId()), partyRequestDto);
        Assertions.assertThat(updatePartyRes.getStatusCode()).isEqualTo(FORBIDDEN);
        Assertions.assertThat(updatePartyRes.getMessage()).isEqualTo(NOT_OWNER_ERROR);
    }


    @Test
    void partyMemberSendChatSuccess() throws Exception {
        mockMvc.perform(get("/members/develop/{memberId}/login", testDto.getPartyMemberId()));

        ChatSendRequestDto chatSendRequestDto = ChatSendRequestDto.builder().content("test message").build();
        DefaultResponse sendMessageRes = controllerTestUtil.
                expectDefaultResponseObject(post("/chats/{partyId}", testDto.getPartyId()), chatSendRequestDto);

        Assertions.assertThat(sendMessageRes.getStatusCode()).isEqualTo(OK);

    }
    */
}