package nbbang.com.nbbang.domain.party.controller;

import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.config.WebMvcConfig;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.security.CustomOAuth2MemberService;
import nbbang.com.nbbang.global.validator.PartyMemberValidator;
import nbbang.com.nbbang.global.validator.PartyMemberValidatorService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;


@WebMvcTest(PartyController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import({ControllerTestUtil.class})
@MockBeans({ @MockBean(WebMvcConfig.class), @MockBean(PartyService.class), @MockBean(MemberService.class),
        @MockBean(CustomOAuth2MemberService.class), @MockBean(CurrentMember.class),
        @MockBean(PartyMemberValidator.class), @MockBean(PartyMemberValidatorService.class),
})
public class ApiTestParent {
}
