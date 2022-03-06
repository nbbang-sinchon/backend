package nbbang.com.nbbang.global.security;

import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import static nbbang.com.nbbang.global.error.GlobalErrorResponseMessage.UNAUTHORIZED_ERROR;
import static nbbang.com.nbbang.global.security.SecurityPolicy.TOKEN_COOKIE_KEY;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private TokenProvider tokenProvider = new TokenProvider();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logRequest(request);
        try {
            processJwtFromRequest(request);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(StatusCode.UNAUTHORIZED, UNAUTHORIZED_ERROR);
        }
    }

    private String processJwtFromRequest(HttpServletRequest request) {
        try {
            Cookie cookie = CookieUtils.getCookie(request, TOKEN_COOKIE_KEY).get();
            String token = cookie.getValue();
            if (tokenProvider.validateToken(token)) {
                Long id = tokenProvider.getUserIdFromToken(token);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(id.toString(), null, null);
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(authentication);
                HttpSession session = request.getSession(true);
                session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
                return token;
            }
            else {
                throw new RuntimeException();
            }

        } catch (Exception e) {
            throw new RuntimeException("INVALID TOKEN");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        if (path.startsWith("/manyparties")) {
            return true;
        }
        if (path.startsWith("/oauth2") || path.startsWith("/login") || path.startsWith("/logout") || path.startsWith("/favicon.ico")) {
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

    private void logRequest(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();

        System.out.println("=============NEWREQUEST===========");
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String nextName = headerNames.nextElement();
                System.out.print(nextName);
                System.out.println(" : "+request.getHeader(nextName));
            }
        }
        System.out.println("-------------COOKIES--------------");
        Cookie [] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                System.out.println(c.getName());
            }
        }
        else {
            System.out.println("empty");
        }
        System.out.println("----------------------------------");
        System.out.println("==================================");
    }

    /*private String processJwtFromRequest(HttpServletRequest request) {
        String token = request.getHeader("access_token");
        Cookie cookie = CookieUtils.getCookie(request, "access_token").get();
        if (cookie != null) {
            token = cookie.getValue();
        }
        try {
            if (tokenProvider.validateToken(token)) {
                Long id = tokenProvider.getUserIdFromToken(token);
                System.out.println("Hello : " + id);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(id.toString(), null, null);
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(authentication);
                HttpSession session = request.getSession(true);
                session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
                System.out.println("Ok, cookie's correct");
            }
            else {
                throw new RuntimeException();
            }

        } catch (Exception e) {
            throw new RuntimeException("HELL");
        }
        return token;
    }*/


}
