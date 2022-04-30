package nbbang.com.nbbang.global.security;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginRedirectUriFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String redirect_uri = request.getParameter("redirect_uri");
        if (redirect_uri != null) {
            addRedirectUriCookie(response, redirect_uri);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        if (path.startsWith("/oauth2") || path.startsWith("/login")) {
            return false;
        }
        return true;
    }

    private void addRedirectUriCookie(HttpServletResponse response, String value) {
        CookieUtils.addResponseCookie(response, "redirect_uri", value, true, true, 300, "none", "", "/");
    }
}
