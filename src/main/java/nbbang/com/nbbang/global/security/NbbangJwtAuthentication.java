package nbbang.com.nbbang.global.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class NbbangJwtAuthentication extends AbstractAuthenticationToken {
    private final Map<String, Object> attributes;

    public NbbangJwtAuthentication(Long memberId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        //this.attributes = Collections.unmodifiableMap(new LinkedHashMap<>());
        this.attributes = new LinkedHashMap<>();
        this.attributes.put("memberId", memberId);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.attributes.get("memberId");
    }

    @Override
    public Object getPrincipal() {
        return this.attributes.get("memberId");
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}
