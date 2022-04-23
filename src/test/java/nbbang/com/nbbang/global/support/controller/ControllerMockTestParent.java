package nbbang.com.nbbang.global.support.controller;

import nbbang.com.nbbang.global.config.WebMvcConfig;
import nbbang.com.nbbang.global.security.CustomOAuth2MemberService;
import nbbang.com.nbbang.global.security.JwtAuthenticationManager;
import nbbang.com.nbbang.global.security.LogoutService;
import nbbang.com.nbbang.global.security.TokenProvider;
import nbbang.com.nbbang.global.security.context.CurrentMember;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

@Import({ControllerMockTestUtil.class})
@MockBeans({
        @MockBean(JpaMetamodelMappingContext.class),
        @MockBean(WebMvcConfig.class),
        @MockBean(CustomOAuth2MemberService.class), @MockBean(CurrentMember.class),
        @MockBean(LogoutService.class),
        @MockBean(JwtAuthenticationManager.class), @MockBean(TokenProvider.class)
})
public abstract class ControllerMockTestParent {
}
