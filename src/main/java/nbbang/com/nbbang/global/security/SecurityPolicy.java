package nbbang.com.nbbang.global.security;

import org.springframework.beans.factory.annotation.Value;

public class SecurityPolicy {
    @Value("${jwt.expire.time:3600000}")
    public static int TOKEN_EXPIRE_TIME;
    @Value("${jwt.cookie.key:access_token}")
    public static String TOKEN_COOKIE_KEY;
    @Value("${login.default.redirection.uri:http://127.0.0.1:3000}")
    public static String DEFAULT_REDIRECT_URI;
    @Value("${jwt.secret.key}") // .gitignore
    public static String TOKEN_SECRET_KEY;
}
