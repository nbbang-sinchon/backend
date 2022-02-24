package nbbang.com.nbbang.global.security;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Login", description = "../oauth2/authorization/google 로 GET 요청시 로그인 페이지로 리다이렉트 되며, /logout 으로 GET 요청시 로그아웃 됩니다.")
@RestController
@RequiredArgsConstructor
public class LoginController {
    @GetMapping("/logout")
    public DefaultResponse logout() {
        return DefaultResponse.res(StatusCode.OK, "로그아웃");
    }
}
