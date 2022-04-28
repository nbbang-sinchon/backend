package nbbang.com.nbbang.global.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final SecurityPolicy securityPolicy;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        addAccessTokenFromRequest(request, response);
        String targetUri = determineTargetUri(request);
        getRedirectStrategy().sendRedirect(request, response, targetUri);
    }

    private void addAccessTokenFromRequest(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        SessionMember member = (SessionMember) session.getAttribute("member");
        String token = tokenProvider.createTokenByMemberId(member.getId());
        addAccessTokenCookie(response, token);
        log.info("Welcome " + member.getId() + " has logged in");
    }

    private void addAccessTokenCookie(HttpServletResponse response, String token) {
        CookieUtils.addResponseCookie(response, securityPolicy.getTokenCookieKey(), token, true, true, securityPolicy.getTokenExpireTime(), "none", "", "/");
    }

    private String determineTargetUri(HttpServletRequest request) {
        String redirect_uri;
        Optional<Cookie> cookie = CookieUtils.getCookie(request, "redirect_uri");
        if (!cookie.isEmpty()) { // Redirect uri specified by client
            redirect_uri = cookie.get().getValue();
        } else { // default
            redirect_uri = securityPolicy.getDefaultRedirectUri();
        }
        return UriComponentsBuilder.fromUriString(redirect_uri)
                .build().toUriString();
    }

}
