package nbbang.com.nbbang.global.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.security.SessionMember;

import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

/*
    메소드가 Thread-safe 한지 확실하지 않아서 thread-local 하게 바꾸는 게 좋을수 있습니다.
 */
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    private final CurrentMember currentMember;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        try {
            //SessionMember sessionMember = (SessionMember) session.getAttribute("member");
            session = request.getSession(false);

            if (session != null) {
                SecurityContext sc = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
                this.currentMember.setMemberId(Long.parseLong((String) sc.getAuthentication().getPrincipal()));
            }

        }
        catch (Exception e) {
            this.currentMember.setMemberId(1L);
        }
        return true;
    }
}
