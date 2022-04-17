package nbbang.com.nbbang.global.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static nbbang.com.nbbang.global.security.SecurityPolicy.TOKEN_COOKIE_KEY;

// https://docs.spring.io/spring-security/reference/6.0/servlet/oauth2/resource-server/jwt.html
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private AuthenticationManager authenticationManager;
    private TokenProvider tokenProvider;
    private LogoutService logoutService;

    @Value("${deploy:false}")
    private Boolean isDeploy;

    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager,
            TokenProvider tokenProvider,
            LogoutService logoutService
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.logoutService = logoutService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getJwtFromRequest(request);
            if (logoutService.isInvalid(token)) {
                log.error("Logout member");
                throw new RuntimeException();
            }
            successAuthentication(token, request);
            log.info("Successfully authenticated");
        } catch (Exception e) {
            log.error("Failed to authenticate");
            if (isDeploy == null || !isDeploy) {
                testAuthentication(request);
            }
        }
        filterChain.doFilter(request, response);
    }
    
    private void testAuthentication(HttpServletRequest request) {
        Long memberId = 1L;
        processAuthentication(memberId.toString(), request);
    }
    
    private void successAuthentication(String token, HttpServletRequest request) {
        Long memberId = tokenProvider.getUserIdFromToken(token);
        processAuthentication(memberId.toString(), request);
    }

    private void processAuthentication(String value, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(value, value);
        Authentication authResult = authenticationManager.authenticate(authReq);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        try {
            Cookie cookie = CookieUtils.getCookie(request, TOKEN_COOKIE_KEY).get();
            String token = cookie.getValue();
            return token;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        if (path.startsWith("/oauth2") || path.startsWith("/login") || path.startsWith("/favicon.ico")) {
            return true;
        }
        if (path.startsWith("/api/oauth2") || path.startsWith("/api/login")) {
            return true;
        }
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/configuration/ui") || path.startsWith("/swagger-resources/**") ||path.startsWith("/configuration/security") || path.startsWith("/swagger-ui.html") || path.startsWith("/webjars/**")) {
            return true;
        }
        return false;
    }
}
