package nbbang.com.nbbang.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenConverter implements AuthenticationConverter {

    private final SecurityPolicy securityPolicy;

    private String getJwtFromRequest(HttpServletRequest request) {
        try {
            Cookie cookie = CookieUtils.getCookie(request, securityPolicy.getTokenCookieKey()).get();
            String token = cookie.getValue();
            return token;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Authentication convert(HttpServletRequest request) {
        String token = getJwtFromRequest(request);
        if (token == null) throw new RuntimeException();
        return new NbbangJwtAuthenticationToken(token);
    }
}
