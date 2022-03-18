package nbbang.com.nbbang.global.support.controller;

import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.config.WebMvcConfig;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.security.CustomOAuth2MemberService;
import nbbang.com.nbbang.global.security.LogoutService;
import nbbang.com.nbbang.global.validator.PartyMemberValidator;
import nbbang.com.nbbang.global.validator.PartyMemberValidatorService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;


@MockBean(JpaMetamodelMappingContext.class)
@Import({ControllerTestUtil.class})
@MockBeans({ @MockBean(WebMvcConfig.class), @MockBean(PartyService.class), @MockBean(MemberService.class),
        @MockBean(CustomOAuth2MemberService.class), @MockBean(CurrentMember.class),
        @MockBean(PartyMemberValidator.class), @MockBean(PartyMemberValidatorService.class),
        //@MockBean(TokenAuthenticationFilter.class),
        @MockBean(LogoutService.class),
})
public class ControllerTestParent {
}
