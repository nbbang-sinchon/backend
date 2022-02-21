package nbbang.com.nbbang.global.security;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    @GetMapping("/logout")
    public DefaultResponse logout() {
        return DefaultResponse.res(StatusCode.OK, "로그아웃");
    }
}
