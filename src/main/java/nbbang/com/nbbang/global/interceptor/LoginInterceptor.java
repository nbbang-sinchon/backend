package nbbang.com.nbbang.global.interceptor;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import nbbang.com.nbbang.global.security.SessionMember;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
