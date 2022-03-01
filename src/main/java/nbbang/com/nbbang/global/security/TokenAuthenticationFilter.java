package nbbang.com.nbbang.global.security;

import antlr.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String jwt = getJwtFromRequest(request);
        System.out.println(jwt);
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String token = request.getHeader("access_token");
        String memberId = request.getHeader("memberid");

        if (tokenProvider.validateToken(token)) {
            UsernamePasswordAuthenticationToken authReq
                    = new UsernamePasswordAuthenticationToken("hello", "None");
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberId, null, null);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
            System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            System.out.println("Safely saved.");
        }

        return token;
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
