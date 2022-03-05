package nbbang.com.nbbang.global.config;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.domain.party.controller.ControllerTestUtil;
import nbbang.com.nbbang.domain.party.controller.PartyController;
import nbbang.com.nbbang.domain.party.service.PartyService;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.security.CustomOAuth2MemberService;
import nbbang.com.nbbang.global.validator.PartyMemberValidator;
import nbbang.com.nbbang.global.validator.PartyMemberValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PartyController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import({ControllerTestUtil.class, TestConfig.class})
public @interface ControllerTest {

}
