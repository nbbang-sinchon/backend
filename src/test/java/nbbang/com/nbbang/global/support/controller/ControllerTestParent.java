package nbbang.com.nbbang.global.support.controller;

import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.config.WebMvcConfig;
import nbbang.com.nbbang.global.security.JwtAuthenticationManager;
import nbbang.com.nbbang.global.security.TokenProvider;
import nbbang.com.nbbang.global.security.context.CurrentMember;
import nbbang.com.nbbang.global.security.CustomOAuth2MemberService;
import nbbang.com.nbbang.global.security.LogoutService;
import nbbang.com.nbbang.global.validator.PartyMemberValidator;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;


@MockBean(JpaMetamodelMappingContext.class)
@Import({ControllerTestUtil.class})
@MockBeans({ @MockBean(WebMvcConfig.class), @MockBean(PartyService.class), @MockBean(MemberService.class),
        @MockBean(CustomOAuth2MemberService.class), @MockBean(CurrentMember.class),
        @MockBean(PartyMemberValidator.class), @MockBean(LogoutService.class),
        @MockBean(JwtAuthenticationManager.class), @MockBean(TokenProvider.class)
})
public abstract class ControllerTestParent {
}
