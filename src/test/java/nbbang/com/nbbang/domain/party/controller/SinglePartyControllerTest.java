package nbbang.com.nbbang.domain.party.controller;

import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.dto.single.request.PartyRequestDto;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.error.ErrorResponse;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.security.context.CurrentMember;
import nbbang.com.nbbang.global.support.controller.ControllerMockTestParent;
import nbbang.com.nbbang.global.support.controller.ControllerMockTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static nbbang.com.nbbang.global.response.StatusCode.BAD_REQUEST;
import static nbbang.com.nbbang.global.response.StatusCode.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc(addFilters = false) // Bypass security filters
@WebMvcTest(PartyController.class)
class SinglePartyControllerTest extends ControllerMockTestParent {
    @Autowired private MockMvc mockMvc;
    @Autowired private ControllerMockTestUtil controllerTestUtil;
    @MockBean private PartyService partyService;
    @MockBean private MemberService memberService;

    @Test
    void createPartyGoalNumberError() throws Exception {
        PartyRequestDto dto = PartyRequestDto.builder().title("hello").place("sinchon").hashtags(Arrays.asList("hello", "wow")).build();
        ErrorResponse res = controllerTestUtil.expectErrorResponseObject(post("/parties"), dto);
        assertThat(res.getStatusCode() == BAD_REQUEST);
    }

    @Test
    void updatePartyGoalNumberSuccess() throws Exception {
        PartyRequestDto dto = PartyRequestDto.builder().title("hello").place("sinchon").hashtags(Arrays.asList("hello", "wow")).build();
        DefaultResponse res = controllerTestUtil.expectDefaultResponseObject(patch("/parties/1"), dto);
        assertThat(res.getStatusCode() == OK);
    }


    @Test
    void hashtagError() throws Exception {
        Member memberA = Member.builder().nickname("memberA").build();
        Party partyA = Party.builder().id(1L).title("partyA").build();
        PartyRequestDto dto = PartyRequestDto.builder().title("hello").place("sinchon").goalNumber(4).hashtags(Arrays.asList(" ", "wow!")).build();


        ErrorResponse createRes = controllerTestUtil.expectErrorResponseObject(post("/parties"), dto);
        assertThat(createRes.getStatusCode() == BAD_REQUEST);

        ErrorResponse updateRes = controllerTestUtil.expectErrorResponseObject(patch("/parties/{partyId}", 1L), dto);
        assertThat(updateRes.getStatusCode() == BAD_REQUEST);
    }
}