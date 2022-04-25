package nbbang.com.nbbang.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements AuthenticationManager {
    private final TokenProvider tokenProvider;
    static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

    static {
        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        if (tokenProvider.validateToken(token)) {
            Long memberId = tokenProvider.getUserIdFromToken(token);
            Authentication authResult = convert(memberId);
            return authResult;
        } else {
            throw new RuntimeException();
        }
    }

    private Authentication convert(Long memberId) {
        Authentication authResult = new NbbangJwtAuthentication(memberId, AUTHORITIES);
        return authResult;
    }
}