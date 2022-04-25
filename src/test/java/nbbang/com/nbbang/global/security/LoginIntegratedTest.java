package nbbang.com.nbbang.global.security;

import nbbang.com.nbbang.domain.member.domain.Member;
import nbbang.com.nbbang.domain.member.dto.Place;
import nbbang.com.nbbang.domain.member.service.MemberService;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.support.controller.ControllerTestUtil;
import nbbang.com.nbbang.global.support.controller.IntegratedTestParent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginIntegratedTest extends IntegratedTestParent {
    @Autowired TestRestTemplate template;
    @Autowired ControllerTestUtil controllerTestUtil;
    @Autowired TokenProvider tokenProvider;

    @MockBean MemberService memberService;

    private final Long mockMemberId = 1L;
    private final String mockNickname = "애기 루피";


    @BeforeEach
    public void createMockData() {
        // Mock member
        Member luffy = Member.createMember(mockNickname, Place.SINCHON);
        // Mock memberService
        when(memberService.findById(mockMemberId)).thenReturn(luffy);
    }

    @Test
    public void authenticatedMemberTest() throws Exception {
        String token = tokenProvider.createTokenByMemberId(mockMemberId);
        Thread.sleep(1000); // to ensure different timestamp encryption
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", "access_token=" + token);
        HttpEntity req = new HttpEntity(headers);
        ResponseEntity res = template.exchange("/members", HttpMethod.GET, req, DefaultResponse.class);
        Map data = controllerTestUtil.expectMapData(res);
        //data.keySet().stream().forEach(k -> {
        //    System.out.print(k + ": ");
        //    System.out.println(data.get(k));
        //});
        assertThat(data.get("nickname")).isEqualTo(mockNickname);
    }

    @Test
    public void unauthenticatedMemberTest() throws Exception {
        String salt = "hdjkrhqwiejnasklc";
        String token = tokenProvider.createTokenByMemberId(mockMemberId) + salt; // Illegal token
        Thread.sleep(1000); // to ensure different timestamp encryption
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", "access_token=" + token);
        HttpEntity req = new HttpEntity(headers);
        ResponseEntity res = template.exchange("/members", HttpMethod.GET, req, DefaultResponse.class);
        DefaultResponse resp = controllerTestUtil.expectDefaultResponseObject(res);
        assertThat(resp.getStatusCode()).isEqualTo(401);
    }

    @Test
    public void logoutMemberTest() throws Exception {
        String token = tokenProvider.createTokenByMemberId(mockMemberId);
        Thread.sleep(1000); // to ensure different timestamp encryption
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", "access_token=" + token);
        HttpEntity req = new HttpEntity(headers);
        ResponseEntity res = template.exchange("/members", HttpMethod.GET, req, DefaultResponse.class);
        Map data = controllerTestUtil.expectMapData(res);
        assertThat(data.get("nickname")).isEqualTo(mockNickname); // 멤버 조회 성공

        ResponseEntity res2 = template.exchange("/gologout", HttpMethod.POST, req, DefaultResponse.class);
        DefaultResponse resp2 = controllerTestUtil.expectDefaultResponseObject(res2);
        assertThat(resp2.getStatusCode()).isEqualTo(200); // 로그아웃 성공

        ResponseEntity res3 = template.exchange("/members", HttpMethod.GET, req, DefaultResponse.class);
        DefaultResponse resp3 = controllerTestUtil.expectDefaultResponseObject(res3);
        assertThat(resp3.getStatusCode()).isEqualTo(401); // 로그아웃한 유저는 멤버 조회할 수 없음
    }

}
