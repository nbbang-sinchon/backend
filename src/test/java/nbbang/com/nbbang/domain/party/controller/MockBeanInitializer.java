package nbbang.com.nbbang.domain.party.controller;

import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.config.TestConfig;
import nbbang.com.nbbang.global.config.WebMvcConfig;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.security.CustomOAuth2MemberService;
import nbbang.com.nbbang.global.validator.PartyMemberValidator;
import nbbang.com.nbbang.global.validator.PartyMemberValidatorService;
import org.springframework.boot.test.mock.mockito.MockBean;

public class MockBeanInitializer {

    @MockBean private WebMvcConfig webMvcConfig;
    @MockBean private PartyService partyService;
    @MockBean private MemberService memberService;
    @MockBean private CustomOAuth2MemberService customOAuth2MemberService;
    @MockBean private CurrentMember currentMember;
    @MockBean private PartyMemberValidator partyMemberValidator;
    @MockBean private PartyMemberValidatorService partyMemberValidatorService;
}
