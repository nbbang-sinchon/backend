package nbbang.com.nbbang.global.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// https://docs.spring.io/spring-security/reference/6.0/servlet/oauth2/resource-server/jwt.html
@Slf4j
@AllArgsConstructor
public final class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final LogoutService logoutService;
    private final SecurityPolicy securityPolicy;
    private final AuthenticationConverter authenticationConverter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            processAuthentication(request);
            log.info("Successfully authenticated");
        } catch (Exception e) {
            log.error("Failed to authenticate");
            if (!securityPolicy.isDeploy()) {
                testAuthentication(request);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void processAuthentication(HttpServletRequest request) {
        Authentication authReq = authenticationConverter.convert(request);
        //if (isLogout((String) authReq.getPrincipal())) throw new RuntimeException();
        if (jwtLogoutCheck(authReq.getPrincipal())) throw new RuntimeException();
        Authentication authResult = authenticationManager.authenticate(authReq);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
    }

    private boolean jwtLogoutCheck(Object obj) {
        try {
            String token = (String) obj;
            return logoutService.isInvalid(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isLogout(String token) {
        return logoutService.isInvalid(token);
    }

    // 루피로 로그인 하기
    private void testAuthentication(HttpServletRequest request) {
        String token = tokenProvider.createTokenByMemberId(1L);
        Authentication authReq = new NbbangJwtAuthenticationToken(token);
        Authentication authResult = authenticationManager.authenticate(authReq);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        log.info("루피로 로그인 합니다. 로컬 테스트 용도인 걸 확인하세요.");
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
