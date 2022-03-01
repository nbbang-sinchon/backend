package nbbang.com.nbbang.global.security;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);
        System.out.println(jwt);
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
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
        return token;
    }
}
