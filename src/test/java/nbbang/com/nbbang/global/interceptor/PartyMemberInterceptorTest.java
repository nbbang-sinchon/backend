package nbbang.com.nbbang.global.interceptor;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.global.support.controller.ControllerTestUtil;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.security.CustomOAuth2MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(PartyMemberInterceptor.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(ControllerTestUtil.class)
@Slf4j
class PartyMemberInterceptorTest {

    @Autowired private ControllerTestUtil controllerTestUtil;

    @MockBean private PartyService partyService;
    @MockBean private MemberService memberService;
    @MockBean private CustomOAuth2MemberService customOAuth2MemberService;
    @MockBean private CurrentMember currentMember;

    @Test
    void test() throws Exception {
        //DefaultResponse res = controllerTestUtil.expectDefaultResponseObject(get("/chats/{partyId}", 70L));
        //log.info("status code: {}",res.getStatusCode());
    }
}