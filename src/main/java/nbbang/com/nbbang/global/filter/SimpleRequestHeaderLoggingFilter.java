package nbbang.com.nbbang.global.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Request header logging filter
 *  OncePerRequestFilter 를 사용하여 servlet re-entry 의 경우 두 번 로그가 찍히는 것을 방지합니다.
 *  application.properties 의 request.logging=true 로 설정하여 킬 수 있습니다.
 *  Body 를 로깅하려면 stream 을 복사하는 방법 등의 추가 구현이 필요합니다.
 */

public class SimpleRequestHeaderLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Enumeration<String> headerNames = request.getHeaderNames();

        System.out.println("=============NEWREQUEST===========");
        System.out.println(request.getMethod());
        System.out.println(request.getRequestURL());
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String nextName = headerNames.nextElement();
                      System.out.print(nextName);
                      System.out.println(" : "+request.getHeader(nextName));
            }
        }
        System.out.println("-------------COOKIES--------------");
        Cookie[] cookies = request.getCookies();
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
        filterChain.doFilter(request, response);
    }
}
