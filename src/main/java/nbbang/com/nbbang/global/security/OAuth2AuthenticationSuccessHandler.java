package nbbang.com.nbbang.global.security;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private String redirect_url = "http://127.0.0.1:8080";
    private String targetUri;
    private TokenProvider tokenProvider = new TokenProvider();
    @Autowired OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {



        targetUri = determineTargetUri(request, response, authentication);
        getRedirectStrategy().sendRedirect(request, response, targetUri);
    }

    private String determineTargetUri(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = tokenProvider.createToken(authentication);
        //CookieUtils.addCookie(response, "access_token", token, 360000);
        //CookieUtils.addCookie(response, "member_id", "2", 360000);
        HttpSession session = request.getSession(false);
        SessionMember member = (SessionMember) session.getAttribute("member");

        return UriComponentsBuilder.fromUriString(redirect_url)
                .queryParam("token", token)
                .queryParam("id", member.getId())
                .build().toUriString();
    }

}
