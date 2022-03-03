package nbbang.com.nbbang.global.security;

import antlr.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.memory.UserAttribute;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Optional;

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
            response.sendError(401, "Unauthorized");
        }
    }

    private String processJwtFromRequest(HttpServletRequest request) {
        String token = request.getHeader("access_token");
        Cookie cookie = CookieUtils.getCookie(request, "access_token").get();
        if (cookie != null) {
            token = cookie.getValue();
            System.out.println("Ok, cookie's exist");
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
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        System.out.println(path);
        if (path.startsWith("/members")) {
            return false;
        }
        if (path.startsWith("/manyparties")) {
            return true;
        }
        if (path.startsWith("/oauth2") || path.startsWith("/login")) {
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
}
