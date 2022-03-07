package nbbang.com.nbbang.domain.party.controller;

import nbbang.com.nbbang.domain.member.controller.MemberController;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.security.CustomOAuth2MemberService;
import nbbang.com.nbbang.global.support.controller.ControllerTestParent;
import nbbang.com.nbbang.global.support.controller.ControllerTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
/*@MockBean(JpaMetamodelMappingContext.class)
@Import(ControllerTestUtil.class)*/
public class LoginInterceptorTest extends ControllerTestParent {
    // 상속받고 있는 controller test parent는 interceptor를 적용하지 않도록 설정합니다.
    // interceptor를 테스트하시려면 controller test parent를 제거하고, 이 클래스에서 필요한 클래스를 @Bockbean으로 받아주세요.

/*    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartyService partyService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private CustomOAuth2MemberService customOAuth2MemberService;

    @MockBean
    private CurrentMember currentMember;*/

    @Test
    public void basicTest() {

    }
}
