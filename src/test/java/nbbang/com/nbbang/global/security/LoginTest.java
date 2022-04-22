package nbbang.com.nbbang.global.security;

import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.global.response.DefaultResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginTest {

    @Autowired TestRestTemplate template;
    @MockBean MemberService memberService;

    @Test
    public void memberLoginTest() {
        // Mock member
        Member luffy = Member.createMember("루피", Place.SINCHON);
        // Mock memberService
        when(memberService.findById(1L)).thenReturn(luffy);

        // Try with Authentication
        DefaultResponse resp = template.withBasicAuth("1L", "1L")
                .getForEntity("/members", DefaultResponse.class).getBody();
        Assertions.assertThat(resp.getStatusCode()).isEqualTo(200);
        System.out.println("success!");
    }
}
