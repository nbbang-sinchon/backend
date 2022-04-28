package nbbang.com.nbbang.global.support.controller;

import nbbang.com.nbbang.global.config.WebMvcConfig;
import nbbang.com.nbbang.global.security.*;
import nbbang.com.nbbang.global.security.context.CurrentMember;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

/**
 * Abstract controller Mock test
 * Test classes extending this will bypass security filters
 */
@Import({ControllerMockTestUtil.class})
@MockBeans({
        @MockBean(JpaMetamodelMappingContext.class),
        @MockBean(WebMvcConfig.class),
        @MockBean(CustomOAuth2MemberService.class), @MockBean(CurrentMember.class),
        @MockBean(LogoutService.class),
        @MockBean(JwtAuthenticationManager.class), @MockBean(TokenProvider.class),
        @MockBean(SecurityPolicy.class), @MockBean(JwtAuthenticationTokenConverter.class)
})
public abstract class ControllerMockTestParent {
}
