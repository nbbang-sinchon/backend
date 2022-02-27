package nbbang.com.nbbang.global.interceptor;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.security.SessionMember;

import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
/*
    메소드가 Thread-safe 한지 확실하지 않아서 thread-local 한 session 으로 바꾸는 게 좋을수 있습니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    private final CurrentMember currentMember;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        try {
            SessionMember sessionMember = (SessionMember) session.getAttribute("member");
            this.currentMember.setMemberId(sessionMember.getId());
        }
        catch (Exception e) {
            this.currentMember.setMemberId(1L);
        }
        return true;
    }
}
