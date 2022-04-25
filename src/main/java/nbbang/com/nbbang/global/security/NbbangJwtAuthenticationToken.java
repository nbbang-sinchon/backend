package nbbang.com.nbbang.global.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class NbbangJwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;

    public NbbangJwtAuthenticationToken(String token) {
        super(Collections.emptyList());
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return this.getToken();
    }

    @Override
    public Object getPrincipal() {
        return this.getToken();
    }
}
