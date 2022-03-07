package nbbang.com.nbbang.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.error.ErrorResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import static nbbang.com.nbbang.global.error.GlobalErrorResponseMessage.UNAUTHORIZED_ERROR;
import static nbbang.com.nbbang.global.security.RequestLogUtils.logRequest;
import static nbbang.com.nbbang.global.security.SecurityPolicy.TOKEN_COOKIE_KEY;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private TokenProvider tokenProvider = new TokenProvider();
    private final LogoutService logoutService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logRequest(request);
        try {
            processJwtFromRequest(request);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            //response.sendError(StatusCode.UNAUTHORIZED, UNAUTHORIZED_ERROR);
            PrintWriter writer = response.getWriter();
            ObjectMapper om = new ObjectMapper();
            writer.print(om.writeValueAsString(ErrorResponse.res(StatusCode.UNAUTHORIZED, UNAUTHORIZED_ERROR, null)));
        }
    }

    private String processJwtFromRequest(HttpServletRequest request) {
        try {
            Cookie cookie = CookieUtils.getCookie(request, TOKEN_COOKIE_KEY).get();
            String token = cookie.getValue();
            String path = request.getServletPath();
            if (logoutService.isInvalid(token)) {
                System.out.println("token is already invalidated");
                throw new RuntimeException();
            }
            if (tokenProvider.validateToken(token)) {
                System.out.println(path);
                if (path.startsWith("/gologout2")) {
                    doLogout(token);
                    throw new RuntimeException();
                }
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
        System.out.println(request.getMethod());
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        if (path.startsWith("/manyparties")) {
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
        System.out.println("trying to logout...");
    }

}
