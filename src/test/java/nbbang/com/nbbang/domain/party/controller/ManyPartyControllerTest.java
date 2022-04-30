package nbbang.com.nbbang.domain.party.controller;

import nbbang.com.nbbang.domain.partymember.domain.PartyMember;
import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.hashtag.domain.Hashtag;
import nbbang.com.nbbang.domain.party.domain.Party;
import nbbang.com.nbbang.domain.hashtag.domain.PartyHashtag;
import nbbang.com.nbbang.domain.party.domain.PartyStatus;
import nbbang.com.nbbang.domain.party.dto.many.PartyListRequestFilterDto;
import nbbang.com.nbbang.domain.party.dto.many.PartyListResponseDto;
import nbbang.com.nbbang.domain.party.service.ManyPartyService;
import nbbang.com.nbbang.global.security.context.CurrentMember;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.support.controller.ControllerMockTestParent;
import nbbang.com.nbbang.global.support.controller.ControllerMockTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static nbbang.com.nbbang.global.response.StatusCode.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ManyPartyController.class)
class ManyPartyControllerTest extends ControllerMockTestParent {
    @Autowired private MockMvc mockMvc;
    @Autowired private ControllerMockTestUtil controllerMockTestUtil;

    @MockBean private ManyPartyService manyPartyService;
    @MockBean private CurrentMember currentMember;

    private Member memberA;
    private Party partyA;

    @BeforeEach
    public void createMockObjects() {
        Member memberA = Member.builder().id(1L).nickname("memberA").build();
        Member memberB = Member.builder().id(2L).nickname("memberB").build();
        Hashtag hashtagA = Hashtag.builder().id(3L).content("치킨").build();
        Hashtag hashtagB = Hashtag.builder().id(4L).content("피자").build();
        Party partyA = Party.builder().id(5L).title("party A").owner(memberA).place(Place.SINCHON)
                .partyHashtags(Arrays.asList(
                        PartyHashtag.builder().id(6L).hashtag(hashtagA).build(),
                        PartyHashtag.builder().id(7L).hashtag(hashtagB).build()))
                .goalNumber(10).status(PartyStatus.OPEN).createTime(LocalDateTime.now())
                .partyMembers(Arrays.asList(
                        PartyMember.builder().member(memberA).build(),
                        PartyMember.builder().member(memberB).build()))
                .build();
        this.partyA = partyA;
        this.memberA = memberA;
    }

    @Test
    public void manyPartyTest1() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);
        //when(manyPartyService.findAllParties(pageable, false, PartyListRequestFilterDto.builder().build(), null, memberA.getId(), null))
        when(manyPartyService.findAllParties(pageable, PartyListRequestFilterDto.builder().build(), null, memberA.getId()))
                .thenReturn(new PageImpl<>(Arrays.asList(partyA), pageable, 1));
        when(currentMember.id()).thenReturn(memberA.getId());
        DefaultResponse res = controllerMockTestUtil.expectDefaultResponseObject(get("/parties"));
        assertThat(res.getStatusCode()).isEqualTo(OK);
        PartyListResponseDto dto = controllerMockTestUtil.convert(res.getData(), PartyListResponseDto.class);

        assertThat(dto.getParties().get(0).getJoinNumber()).isEqualTo(2);
        assertThat(dto.getParties().get(0).getHashtags().size()).isEqualTo(2);

    }

}