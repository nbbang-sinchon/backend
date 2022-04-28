package nbbang.com.nbbang.global.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityPolicy {

    @Value("${jwt.expire.time:3600000}")
    private int TOKEN_EXPIRE_TIME;

    @Value("${jwt.cookie.key:access_token}")
    private String TOKEN_COOKIE_KEY;

    @Value("${login.default.redirection.uri:http://127.0.0.1:3000}")
    private String DEFAULT_REDIRECT_URI;

    @Value("${deploy}")
    private Boolean IS_DEPLOY;

    @Value("${jwt.secret.key}") // .gitignore
    private String TOKEN_SECRET_KEY;

    public int getTokenExpireTime() {
        return TOKEN_EXPIRE_TIME;
    }

    public String getTokenCookieKey() {
        return TOKEN_COOKIE_KEY;
    }

    public String getDefaultRedirectUri() {
        return DEFAULT_REDIRECT_URI;
    }

    public String getTokenSecretKey() {
        return TOKEN_SECRET_KEY;
    }

    public boolean isDeploy() {
        if (IS_DEPLOY) log.info("App's on deploy");
        else log.info("App's on local");
        return IS_DEPLOY;
    }

}
