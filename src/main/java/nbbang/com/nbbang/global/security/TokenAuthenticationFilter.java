package nbbang.com.nbbang.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.error.ErrorResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Security;
import java.util.Optional;

import static nbbang.com.nbbang.global.error.GlobalErrorResponseMessage.UNAUTHORIZED_ERROR;
import static nbbang.com.nbbang.global.security.RequestLogUtils.logRequest;
import static nbbang.com.nbbang.global.security.SecurityPolicy.TOKEN_COOKIE_KEY;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private TokenProvider tokenProvider = new TokenProvider();
    private AntPathMatcher matcher = new AntPathMatcher();
    private AntPathRequestMatcher partiesMatcher = new AntPathRequestMatcher("/parties/**", HttpMethod.GET.toString());
    private final LogoutService logoutService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logRequest(request);
        System.out.println(request.getRequestURL());
        try {
            String token = getJwtFromRequest(request);
            if (isValid(token) && !isLogout(token)) {
                //processAuthentication(token, request);
                successAuthentication(token, request);
                log.info("Successfully Authenticated!");
                filterChain.doFilter(request, response);
            } else throw new RuntimeException();
        } catch (Exception e) {

            // 로컬용
            log.error("테스트 용도로 1L 멤버로 인증합니다.");
            testPurposeAuthentication(request);
            filterChain.doFilter(request, response);


            // 실제 배포용

            /*if (partiesMatcher.matches(request)) {
                nullAuthentication(request);
                log.info("/GET /parties/** allowed for anonymous users");
                filterChain.doFilter(request, response);
                return;
            }

            log.error("Failed to authenticate");
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            ObjectMapper om = new ObjectMapper();
            writer.print(om.writeValueAsString(ErrorResponse.res(StatusCode.UNAUTHORIZED, UNAUTHORIZED_ERROR, null)));
            */
        }
    }

    private void nullAuthentication(HttpServletRequest request) {
        processAuthentication(null, request);
    }

    private void testPurposeAuthentication(HttpServletRequest request) {
        Long memberId = 1L;
        processAuthentication(memberId.toString(), request);
    }

    private void successAuthentication(String token, HttpServletRequest request) {
        Long memberId = tokenProvider.getUserIdFromToken(token);
        processAuthentication(memberId.toString(), request);
    }

    private void processAuthentication(String value, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(value, null);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
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

    private boolean isValid(String token) {
        return tokenProvider.validateToken(token);
    }

    private boolean isLogout(String token) {
        return logoutService.isInvalid(token);
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

    private void doLogout(String token) {
        logoutService.invalidate(token);
    }

}

