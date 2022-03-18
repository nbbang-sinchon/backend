package nbbang.com.nbbang.global.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private String targetUri;
    private TokenProvider tokenProvider = new TokenProvider();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        targetUri = determineTargetUri(request, response, authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        getRedirectStrategy().sendRedirect(request, response, targetUri);
    }

    private String determineTargetUri(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession(false);
        SessionMember member = (SessionMember) session.getAttribute("member");
        log.info("Welcome " + member.getId() + " has logged in");
        String token = tokenProvider.createToken(authentication, member.getId());
        addAccessTokenCookie(response, token);
        String redirect_uri = DEFAULT_REDIRECT_URI;
        Optional<Cookie> cookie = CookieUtils.getCookie(request, "redirect_uri");
        if (!cookie.isEmpty()) {
            redirect_uri = cookie.get().getValue();
        }
        return UriComponentsBuilder.fromUriString(redirect_uri)
                .build().toUriString();
    }

    private void addAccessTokenCookie(HttpServletResponse response, String token) {
        CookieUtils.addResponseCookie(response, TOKEN_COOKIE_KEY, token, true, true, TOKEN_EXPIRE_TIME, "none", "", "/");
    }

}
