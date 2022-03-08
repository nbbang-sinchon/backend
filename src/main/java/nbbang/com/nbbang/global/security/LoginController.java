package nbbang.com.nbbang.global.security;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.error.ErrorResponse;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Enumeration;

import static nbbang.com.nbbang.global.security.SecurityPolicy.*;

@Tag(name = "Login", description = "로그인 GET 요청시 로그인 페이지로 리다이렉트 되며, /logout 으로 GET 요청시 로그아웃 됩니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LogoutService logoutService;

    @Operation(summary = "구글 로그인", description = "로그인 페이지로 이동합니다(구글). 올바른 요청 시 자신의 정보를 리턴합니다. 직접 DNS 주소로 GET 을 보내세요. (IP 주소가 아닌 DNS 주소)")
    @GetMapping("/oauth2/authorization/google")
    public void memberLogin() {
        System.out.println("login");
    }

    @Operation(summary = "카카오 로그인", description = "로그인 페이지로 이동합니다(카카오). 올바른 요청 시 자신의 정보를 리턴합니다. 직접 DNS 주소로 GET 을 보내세요. (IP 주소가 아닌 DNS 주소)")
    @GetMapping("/oauth2/authorization/kakao")
    public void memberLoginKakao() {
        log.info("kakao login");
    }

    @PostMapping("/gologout")
    public DefaultResponse logout(HttpServletRequest request, HttpServletResponse response) {
        String message = "로그아웃 성공";
        try {
            Cookie cookie = CookieUtils.getCookie(request, TOKEN_COOKIE_KEY).get();
            String token = cookie.getValue();
            logoutService.invalidate(token);
        } catch (Exception e) {
            message = "로그아웃 실패";
        }
        return DefaultResponse.res(StatusCode.OK ,message);
    }
}
