package nbbang.com.nbbang.global.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

import static nbbang.com.nbbang.global.security.SecurityPolicy.*;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private String targetUri;
    private TokenProvider tokenProvider = new TokenProvider();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        RequestLogUtils.logRequest(request);
        System.out.println(request.getParameter("redirect"));
        targetUri = determineTargetUri(request, response, authentication);
        getRedirectStrategy().sendRedirect(request, response, targetUri);
    }

    private String determineTargetUri(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession(false);
        SessionMember member = (SessionMember) session.getAttribute("member");
        String token = tokenProvider.createToken(authentication, member.getId());
        addAccessTokenCookie(response, token);
        //localhostAccessToken(response, token);
        String redirect_uri = DEFAULT_REDIRECT_URI;
        Optional<Cookie> cookie = CookieUtils.getCookie(request, "redirect_uri");
        if (!cookie.isEmpty()) {
            System.out.println("Custom redirect uri");
            redirect_uri = cookie.get().getValue();
        }
        return UriComponentsBuilder.fromUriString(redirect_uri)
                .build().toUriString();
    }

    private void addAccessTokenCookie(HttpServletResponse response, String token) {
        CookieUtils.addResponseCookie(response, TOKEN_COOKIE_KEY, token, true, true, TOKEN_EXPIRE_TIME, "none", FRONTEND_DOMAIN, "/");
    }

    // 로컬 테스팅 용도
    private void localhostAccessToken(HttpServletResponse response, String token) {
        CookieUtils.addResponseCookie(response, TOKEN_COOKIE_KEY, token, false, false, TOKEN_EXPIRE_TIME, "lax", FRONTEND_DOMAIN, "/");
    }

}
