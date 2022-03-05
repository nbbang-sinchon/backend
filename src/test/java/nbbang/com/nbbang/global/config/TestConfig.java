package nbbang.com.nbbang.global.config;


import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.controller.ControllerTestUtil;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.security.CustomOAuth2MemberService;
import nbbang.com.nbbang.global.validator.PartyMemberValidator;
import nbbang.com.nbbang.global.validator.PartyMemberValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


public class TestConfig implements WebMvcConfigurer {

    @MockBean private WebMvcConfig webMvcConfig;
    @MockBean private PartyService partyService;
    @MockBean private MemberService memberService;
    @MockBean private CustomOAuth2MemberService customOAuth2MemberService;
    @MockBean private CurrentMember currentMember;
    @MockBean private PartyMemberValidator partyMemberValidator;
    @MockBean private PartyMemberValidatorService partyMemberValidatorService;
}