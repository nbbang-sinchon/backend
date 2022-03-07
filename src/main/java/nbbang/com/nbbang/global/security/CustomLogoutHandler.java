package nbbang.com.nbbang.global.security;

import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.request.RequestLogUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final LogoutService logoutService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        RequestLogUtils.logRequest(request);
        System.out.println("djaskldjaskldjalksjdklasjdlkasjlkdas");
    }
}