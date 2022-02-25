package nbbang.com.nbbang.domain.party.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.security.CustomOAuth2MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartyController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PartyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartyService partyService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private CustomOAuth2MemberService customOAuth2MemberService;

    @MockBean
    private CurrentMember currentMember;

    private Map toData(MvcResult res) throws Exception {
        if (res == null) {
            return null;
        }
        String json = res.getResponse().getContentAsString();
        DefaultResponse resp = new ObjectMapper().readValue(json, DefaultResponse.class);
        Map data = (Map) resp.getData();
        return data;
    }


    @Test
    public void partyReadTest1() throws Exception {
        Member memberA = Member.builder().nickname("memberA").place(Place.NONE).build();
        Party partyA = Party.builder().id(1L).title("partyA").owner(memberA).goalNumber(10).status(PartyStatus.OPEN).place(Place.NONE).build();
        when(partyService.findById(partyA.getId())).thenReturn(partyA);
        this.mockMvc.perform(get("/parties/partyA")).andExpect(status().isBadRequest());
    }

    @Test
    public void partyReadTest2() throws Exception {
        Member memberA = Member.builder().nickname("memberA").place(Place.NONE).build();
        Party partyA = Party.builder().id(1L).title("partyA").owner(memberA).goalNumber(10).status(PartyStatus.OPEN).place(Place.NONE).build();
        when(partyService.findById(partyA.getId())).thenReturn(partyA);

        MvcResult partyA1 = this.mockMvc.perform(get("/parties/" + partyA.getId())).andExpect(status().isOk())
                //.andExpect(jsonPath("$.title").value("partyA"))
                .andReturn();
        Map data = toData(partyA1);
        assertThat(data.get("title").equals("partyA"));

    }






    /*@Test
    public void partyReadTest1() throws JSONException {
        Member memberA = Member.builder().nickname("memberA").build();
        memberRepository.save(memberA);
        Party partyA = Party.builder().title("partyA").owner(memberA).goalNumber(10).status(PartyStatus.OPEN).build();
        partyRepository.save(partyA);
        Party found = partyRepository.findById(partyA.getId()).get();
        System.out.println(found.getTitle());
        Party find = partyService.findById(partyA.getId());
        System.out.println(find.getTitle());
        //JSONObject obj = this.restTemplate.getForObject("http://localhost:" + port + "/parties/" + partyA.getId(), JSONObject.class);
       //System.out.println(obj.get("title"));
    }*/
}