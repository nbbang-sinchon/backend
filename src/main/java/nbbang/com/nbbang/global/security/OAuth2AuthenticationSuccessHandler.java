package nbbang.com.nbbang.global.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private String redirect_url = "http://127.0.0.1:3000";
    private String targetUri;
    private TokenProvider tokenProvider = new TokenProvider();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        targetUri = determineTargetUri(request, response, authentication);
        getRedirectStrategy().sendRedirect(request, response, targetUri);
    }

    private String determineTargetUri(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession(false);
        SessionMember member = (SessionMember) session.getAttribute("member");
        String token = tokenProvider.createToken(authentication, member.getId());
        //CookieUtils.addCookie(response, "access_token", token, 36000000);
        CookieUtils.addResponseCookie(response, token);
        return UriComponentsBuilder.fromUriString(redirect_url)
                .queryParam("token", token)
                //.queryParam("id", member.getId())
                .build().toUriString();
    }

}
