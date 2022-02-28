package nbbang.com.nbbang.global.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

@Service("loginSuccessHandler")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStratgy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Enumeration<String> headerNames = request.getHeaderNames();

        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String ele = headerNames.nextElement();
                System.out.println(ele);
                System.out.println("Header: " + request.getHeader(ele));
            }
        }
        HttpSession session = request.getSession(false);
        if(session==null) return;
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        //redirectStratgy.sendRedirect(request, response, authentication);
        String redirectUri = request.getHeader("referer");
        if (request.getHeader("redirect") != null) {
            redirectUri = request.getHeader("redirect");
        }
        String prevPage = (String) request.getSession().getAttribute("prevPage");
        System.out.println(prevPage);

        if (prevPage != null) {
            request.getSession().removeAttribute("prevPage");
            redirectUri = prevPage;
        }
        redirectUri = "http://15.165.132.250/main";
        response.sendRedirect(redirectUri);
    }



}
