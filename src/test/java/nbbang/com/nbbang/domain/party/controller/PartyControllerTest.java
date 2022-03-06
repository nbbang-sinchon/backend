package nbbang.com.nbbang.domain.party.controller;

import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.support.controller.ControllerTestParent;
import nbbang.com.nbbang.global.support.controller.ControllerTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static nbbang.com.nbbang.global.response.StatusCode.OK;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartyController.class)
class PartyControllerTest extends ControllerTestParent {

    @Autowired private MockMvc mockMvc;
    @Autowired private ControllerTestUtil controllerTestUtil;

    @MockBean private PartyService partyService;

    @Test
    public void partyReadTest() throws Exception {

    }

    @Test
    public void partyReadTest1() throws Exception {
        String ownerNickname = "루피";

        Member memberA = Member.builder().nickname(ownerNickname).place(Place.NONE).build();
        Party partyA = Party.builder().id(1L).title("partyA").owner(memberA).goalNumber(10).status(PartyStatus.OPEN).place(Place.NONE).build();
        when(partyService.findById(partyA.getId())).thenReturn(partyA);
        DefaultResponse res = controllerTestUtil.expectDefaultResponseObject(get("/parties/{partyId}", partyA.getId()));
        assertThat(res.getStatusCode() == OK);

        Map findParty = controllerTestUtil.expectMapData(res);
        Map findOwner = (Map)findParty.get("owner");

        assertThat(findParty.get("title")).isEqualTo("partyA");
        assertThat(findOwner.get("nickname")).isEqualTo("루피");

    }

    @Test
    public void partyReadTest2() throws Exception {
        Member memberA = Member.builder().nickname("memberA").place(Place.NONE).build();
        Party partyA = Party.builder().id(1L).title("partyA").owner(memberA).goalNumber(10).status(PartyStatus.OPEN).place(Place.NONE).build();
        when(partyService.findById(partyA.getId())).thenReturn(partyA);

        MvcResult partyA1 = this.mockMvc.perform(get("/parties/" + partyA.getId())).andExpect(status().isOk())
                //.andExpect(jsonPath("$.title").value("partyA"))
                .andReturn();
        Map data = controllerTestUtil.expectMapData(partyA1);
        assertThat(data.get("title").equals("partyA"));

    }

}